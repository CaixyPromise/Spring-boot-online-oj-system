package com.caixy.onlineJudge.auth.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.caixy.onlineJudge.auth.factory.OAuthFactory;
import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.common.objectMapper.ObjectMapperUtil;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.CommonConstant;
import com.caixy.onlineJudge.models.dto.email.EmailLoginRequest;
import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.oauth.github.GetAuthorizationUrlRequest;
import com.caixy.onlineJudge.models.dto.user.UserActiveAccountRequest;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.oauth2.OAuthProviderEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.caixy.onlineJudge.models.enums.user.UserGenderEnum;
import com.caixy.onlineJudge.models.vo.user.LoginUserVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.captcha.CaptchaFacadeService;
import com.caixy.serviceclient.service.captcha.request.VerifyPhotoCodeDubboDTO;
import com.caixy.serviceclient.service.captcha.response.CaptchaOperatorResponse;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.request.condition.UserEmailAndPassQueryCondition;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * 鉴权接口控制器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.controller.AuthController
 * @since 2024/8/1 上午1:40
 */
@Slf4j
@RestController
@RequestMapping("/")
public class AuthController
{
    @Resource
    private UserFacadeService userFacadeService;

    @Resource
    private CaptchaFacadeService captchaFacadeService;

    @Resource
    private OAuthFactory oAuthFactory;

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;
    @Resource
    private RedisUtils redisUtils;

    @GetMapping("/get/login/me")
    public BaseResponse<UserVO> getLoginMe()
    {
        StpUtil.checkLogin();
        Object loginId = StpUtil.getLoginId();
        UserVO user = (UserVO) StpUtil.getSessionByLoginId(loginId).get(loginId.toString());
        return ResultUtils.success(user);
    }


    @PostMapping("/register")
    public BaseResponse<Boolean> emailRegister(@RequestBody EmailRegisterRequest emailRegisterRequest)
    {
        String password = emailRegisterRequest.getPassword();
        String checkPassword = emailRegisterRequest.getCheckPassword();
        // 校验两次密码是否一致
        ThrowUtils.throwIf(!password.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        // 校验缓存
        String code = emailRegisterRequest.getCode();

        CaptchaOperatorResponse captchaOperatorResponse = captchaFacadeService.verifyPhotoCaptcha(
                VerifyPhotoCodeDubboDTO.of(code));
        if (!captchaOperatorResponse.getData())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 注册
        UserOperatorResponse register = userFacadeService.preRegister(emailRegisterRequest);
        return ResultUtils.success(register.isSucceed());
    }

    /**
     * 激活用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午2:39
     */
    @PostMapping("/active")
    public BaseResponse<Boolean> activeUserAccount(@RequestBody @Valid UserActiveAccountRequest activeAccountRequest)
    {
        UserGenderEnum userGenderEnum = UserGenderEnum.getEnumByValue(activeAccountRequest.getUserGender());
        if (userGenderEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别参数错误");
        }
        // 校验token
        Map<String, Object> tokenMap = redisUtils.getHashMap(RedisKeyEnum.ACTIVE_USER, String.class, Object.class,
                activeAccountRequest.getActiveToken());
        if (tokenMap == null)
        {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "token不存在");
        }
        // 转换用户实体
        User userInfo = ObjectMapperUtil.convertValue(tokenMap, User.class);
        UserOperatorResponse userOperatorResponse = userFacadeService.activeUserAccount(userInfo,
                activeAccountRequest.getNickName(), userGenderEnum.getValue());
        Boolean isSucceed = userOperatorResponse.isSucceed();
        // 删除缓存
        if (isSucceed)
        {
            redisUtils.delete(RedisKeyEnum.ACTIVE_USER_TOKEN, activeAccountRequest.getTempToken());
            redisUtils.delete(RedisKeyEnum.ACTIVE_USER, activeAccountRequest.getActiveToken());
        }
        return ResultUtils.success(isSucceed);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest)
    {
        String email = emailLoginRequest.getEmail();
        UserQueryFacadeRequest userQueryFacadeRequest = UserQueryFacadeRequest.build(
                new UserEmailAndPassQueryCondition(email, emailLoginRequest.getPassword()));
        UserQueryResponse<UserVO> userVOUserQueryResponse = userFacadeService.query(userQueryFacadeRequest);
        UserVO userInfo = userVOUserQueryResponse.getData();
        // 登录
        if (userVOUserQueryResponse.isSucceed() && userInfo != null)
        {
            doLogin(userInfo);
            return ResultUtils.success(buildLoginUserVO(userInfo));
        }
        else
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }
    }

    /**
     * 退出
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/10 下午10:38
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout()
    {
        StpUtil.logout();
        return ResultUtils.success(true);
    }


    // region 登录相关
    @GetMapping("/oauth2/{provider}/login")
    public BaseResponse<String> initOAuthLogin(
            @PathVariable String provider,
            @ModelAttribute GetAuthorizationUrlRequest authorizationUrlRequest,
            HttpServletRequest request)
    {
        OAuthProviderEnum providerEnum = getOAuthProvider(provider);
        authorizationUrlRequest.setSessionId(request.getSession().getId());
        log.info("authorizationUrlRequest: {}", authorizationUrlRequest);
        String authorizationUrl = oAuthFactory.getOAuth2ActionStrategy(providerEnum).getAuthorizationUrl(
                authorizationUrlRequest);
        return ResultUtils.success(authorizationUrl);
    }

    @GetMapping("/oauth2/{provider}/callback")
    public void oauthLoginCallback(
            @PathVariable("provider") String provider,
            @RequestParam Map<String, Object> allParams,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        allParams.put("sessionId", request.getSession().getId());
        try
        {
            OAuthProviderEnum providerEnum = getOAuthProvider(provider);
            OAuthResultDTO oAuthResultDTO = oAuthFactory.doAuth(providerEnum, allParams);
            if (oAuthResultDTO.isSuccess())
            {
                UserOperatorResponse userOperatorResponse = userFacadeService.doOAuthLogin(oAuthResultDTO);
                UserVO userInfo = userOperatorResponse.getData();
                if (userOperatorResponse.isSucceed() && userInfo != null)
                {
                    doLogin(userInfo);
                }
            }
            response.sendRedirect(oAuthResultDTO.getRedirectUrl());
        }
        catch (Exception e)
        {
            response.sendRedirect(CommonConstant.FRONTED_URL);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    private OAuthProviderEnum getOAuthProvider(String providerName)
    {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.getProviderEnum(providerName);
        if (providerEnum == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的OAuth2登录方式");
        }
        return providerEnum;
    }

    private void doLogin(UserVO userInfo)
    {
        StpUtil.login(userInfo.getId(),
                new SaLoginModel()
                        .setIsLastingCookie(true)
                        .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
        StpUtil.getSession().set(userInfo.getId().toString(), userInfo);
    }

    private LoginUserVO buildLoginUserVO(UserVO userVO)
    {
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(userVO.getId());
        loginUserVO.setToken(StpUtil.getTokenValue());
        loginUserVO.setTokenExpiration(StpUtil.getTokenSessionTimeout());
        return loginUserVO;
    }
}
