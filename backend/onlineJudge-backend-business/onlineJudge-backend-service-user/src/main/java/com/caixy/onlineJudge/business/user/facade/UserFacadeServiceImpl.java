package com.caixy.onlineJudge.business.user.facade;

import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.onlineJudge.common.rpc.facade.Facade;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

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

    @Facade
    @Override
    public UserOperatorResponse doOAuthLogin(OAuthResultDTO oAuthResultDTO)
    {
        return userService.doAuthLogin(oAuthResultDTO);
    }

    @Facade
    @Override
    public UserOperatorResponse register(EmailRegisterRequest emailRegisterRequest)
    {
        return userService.registerByEmail(emailRegisterRequest.getEmail(), emailRegisterRequest.getPassword());
    }

    @Facade
    @Override
    public UserQueryResponse<UserVO> queryByEmail(String email)
    {
        return new UserQueryResponse<>(ErrorCode.SUCCESS, userService.findByEmail(email));
    }

    @Facade
    @Override
    public UserQueryResponse<UserVO> queryByEmailAndPass(String email, String password)
    {
        UserVO byEmailAndPass = userService.findByEmailAndPass(email, password);
        if (byEmailAndPass != null)
        {
            return new UserQueryResponse<>(ErrorCode.SUCCESS, byEmailAndPass);
        }
        return new UserQueryResponse<>(ErrorCode.NOT_FOUND_ERROR, null);
    }
}
