package com.caixy.onlineJudge.auth.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.caixy.onlineJudge.auth.factory.OAuthFactory;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.CommonConstants;
import com.caixy.onlineJudge.models.dto.email.EmailLoginRequest;
import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.oauth.github.GithubGetAuthorizationUrlRequest;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import com.caixy.onlineJudge.models.enums.oauth2.OAuthProviderEnum;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.captcha.CaptchaFacadeService;
import com.caixy.serviceclient.service.captcha.request.VerifyEmailCodeDubboDTO;
import com.caixy.serviceclient.service.captcha.response.CaptchaOperatorResponse;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/register")
    public BaseResponse<Boolean> emailRegister(@RequestBody EmailRegisterRequest emailRegisterRequest)
    {
        String password = emailRegisterRequest.getPassword();
        String checkPassword = emailRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(!password.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        // 校验缓存
        String email = emailRegisterRequest.getEmail();
        String code = emailRegisterRequest.getCode();

        CaptchaOperatorResponse<Boolean> captchaOperatorResponse = captchaFacadeService.verifyEmailCaptcha(
                VerifyEmailCodeDubboDTO.of(code, email, EmailSenderCategoryEnum.REGISTER.getCode()));
        if (!captchaOperatorResponse.getData())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 注册
        UserOperatorResponse register = userFacadeService.register(emailRegisterRequest);
        return ResultUtils.success(register.isSucceed());
    }


    @PostMapping("/login")
    public BaseResponse<Boolean> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest)
    {
        String email = emailLoginRequest.getEmail();
        UserQueryResponse<UserVO> userVOUserQueryResponse = userFacadeService.queryByEmailAndPass(email,
                emailLoginRequest.getPassword());
        if (userVOUserQueryResponse.isSucceed())
        {
            UserVO userInfo = userVOUserQueryResponse.getData();
            doLogin(userInfo);
            return ResultUtils.success(true);
        }
        else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }
    }


    // region 登录相关
    @GetMapping("/oauth2/{provider}/login")
    public BaseResponse<String> initiateGithubLogin(
            @PathVariable String provider,
            @ModelAttribute GithubGetAuthorizationUrlRequest authorizationUrlRequest,
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
    public void githubLoginCallback(
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
            response.sendRedirect(CommonConstants.FRONTED_URL);
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
}
