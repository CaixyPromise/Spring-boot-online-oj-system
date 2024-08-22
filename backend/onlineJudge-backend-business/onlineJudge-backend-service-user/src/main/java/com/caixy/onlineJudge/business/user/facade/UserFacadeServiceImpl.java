package com.caixy.onlineJudge.business.user.facade;

import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.user.*;
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

    @Override
    public UserOperatorResponse doOAuthLogin(OAuthResultDTO oAuthResultDTO)
    {
        return userService.doAuthLogin(oAuthResultDTO);
    }

    @Override
    public UserOperatorResponse login(UserLoginRequest userLoginRequest)
    {
//        userService.userLogin(userLoginRequest);
        return null;
    }

    @Override
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest)
    {
        return null;
    }

    @Override
    public UserOperatorResponse modify(UserUpdateMyRequest userModifyRequest)
    {
        return null;
    }

    @Override
    public UserQueryResponse<UserVO> query(UserQueryRequest userLoginRequest)
    {
        return null;
    }
}
