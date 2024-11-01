package com.caixy.onlineJudge.business.user.facade;

import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.onlineJudge.common.rpc.facade.Facade;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.convertor.user.UserConvertor;
import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 内部用户服务调用实现类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.user_service.service.inner.InnerUserFeignClientImpl
 * @since 2024-07-11 23:27
 **/
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService
{
    @Resource
    private UserService userService;

    /**
     * OAuth登录
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午1:52
     */
    @Facade
    @Override
    public UserOperatorResponse doOAuthLogin(OAuthResultDTO oAuthResultDTO)
    {
        return userService.doAuthLogin(oAuthResultDTO);
    }

    /**
     * 用户预注册
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午1:53
     */
    @Facade
    @Override
    public UserOperatorResponse preRegister(EmailRegisterRequest emailRegisterRequest)
    {
        return userService.preRegisterByEmail(emailRegisterRequest.getEmail(), emailRegisterRequest.getPassword());
    }

    /**
     * 激活用户账号
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午1:57
     */
    @Facade
    @Override
    public UserOperatorResponse activeUserAccount(User userInfo, String nickName, Integer userGender)
    {
        return userService.activeUserAccount(userInfo, nickName, userGender);
    }

    @Facade
    @Override
    public UserQueryResponse<UserVO> query(UserQueryFacadeRequest userQueryFacadeRequest)
    {
        User user = userService.query(userQueryFacadeRequest);
        UserQueryResponse<UserVO> response = new UserQueryResponse<>();

        if (user != null)
        {
            response.setCode(ErrorCode.SUCCESS.getCode());
            UserVO userInfo = UserConvertor.INSTANCE.convert(user);
            response.setData(userInfo);
        }
        else
        {
            response.setCode(ErrorCode.NOT_FOUND_ERROR.getCode());
            response.setData(null);
        }
        return response;
    }

    @Facade
    @Override
    public UserQueryResponse<List<User>> queryUserByIds(Collection<Long> userIds)
    {
        List<User> users = userService.listByIds(userIds);
        UserQueryResponse<List<User>> response = new UserQueryResponse<>();
        if (users.isEmpty())
        {
            response.setCode(ErrorCode.NOT_LOGIN_ERROR.getCode());
            response.setData(Collections.emptyList());
        }
        else
        {
            response.setCode(ErrorCode.SUCCESS.getCode());
            response.setData(users);
        }
        return response;
    }
}
