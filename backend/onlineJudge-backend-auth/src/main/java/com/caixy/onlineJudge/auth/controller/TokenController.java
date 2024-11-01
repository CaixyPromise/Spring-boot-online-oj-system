package com.caixy.onlineJudge.auth.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;

import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.cache.redis.annotation.RateLimitFlow;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.objectMapper.ObjectMapperUtil;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisLimiterEnum;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.request.condition.UserNickNameQueryCondition;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * Token分发接口控制器
 * 包含部分关于token才能操作的关键数据接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.controller.TokenController
 * @since 2024/9/6 上午2:01
 */
@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController
{
    @Resource
    private UserFacadeService userFacadeService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 用于校验激活token，同时生成一个临时允许查询用户名的token
     *
     * @return 用户邮箱，用于前端显示
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 下午3:48
     */
    @GetMapping("/fetch/active/{token}")
    @RateLimitFlow(key = RedisLimiterEnum.REGISTER_TEMP_TOKEN, args = "#token", errorMessage = "已获取临时token")
    public BaseResponse<String> getActiveToken(@PathVariable String token, HttpServletResponse response)
    {
        Map<String, Object> tokenMap = redisUtils.getHashMap(RedisKeyEnum.ACTIVE_USER, String.class, Object.class,
                token);
        if (tokenMap == null)
        {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "token不存在");
        }
        User userInfo = ObjectMapperUtil.convertValue(tokenMap, User.class);
        Date createTime = userInfo.getCreateTime();
        LocalDateTime createDateTime = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        // 检查从createTime到现在是否已超过24小时
        if (ChronoUnit.HOURS.between(createDateTime, now) >= 24)
        {
            // 把redis里的用户信息删除
            redisUtils.delete(RedisKeyEnum.ACTIVE_USER, token);
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "token已过期");
        }
        // 没过期，生成一个新的临时token用于创建用户名
        String tempToken = DigestUtil.md5Hex(
                userInfo.getUserEmail() + System.currentTimeMillis() + RandomUtil.randomNumbers(5)
        );
        redisUtils.setString(RedisKeyEnum.ACTIVE_USER_TOKEN, userInfo.getUserEmail(), tempToken);
        // 新token放在header
        response.setHeader("searchToken", tempToken);
        // 返回用户邮箱
        return ResultUtils.success(userInfo.getUserEmail());
    }

    @GetMapping("/search/nickName")
    public BaseResponse<Boolean> searchNickName(@RequestParam(name = "name") String nickName,
                                                HttpServletRequest request)
    {
        if (StringUtils.isBlank(nickName))
        {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "nickName不能为空");
        }
        String searchToken = request.getHeader("searchToken");
        if (StringUtils.isBlank(searchToken))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "token不存在");
        }
        String tokenIsExist = redisUtils.getString(RedisKeyEnum.ACTIVE_USER_TOKEN, searchToken);
        if (StringUtils.isBlank(tokenIsExist))
        {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "token不存在");
        }
        UserQueryFacadeRequest userQueryFacadeRequest = UserQueryFacadeRequest.build(new UserNickNameQueryCondition(nickName));
        UserQueryResponse<UserVO> userVOUserQueryResponse = userFacadeService.query(userQueryFacadeRequest);
        return ResultUtils.success(userVOUserQueryResponse.getData() != null);
    }


}
