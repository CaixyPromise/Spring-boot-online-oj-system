package com.caixy.serviceclient.service.user;

import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.user.*;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;

/**
 * 用户接口远程调用类
 *
 * @name: com.caixy.serviceclient.service.user.UserFeignClient
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 **/
public interface UserFacadeService
{
    UserOperatorResponse doOAuthLogin(OAuthResultDTO oAuthResultDTO);

    /**
     * 用户登录
     * @param userLoginRequest
     */
    UserOperatorResponse login(UserLoginRequest userLoginRequest);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户信息修改
     * @param userModifyRequest
     * @return
     */
    UserOperatorResponse modify(UserUpdateMyRequest userModifyRequest);

    /**
     * 用户信息查询
     * @param userLoginRequest
     * @return
     */
    UserQueryResponse<UserVO> query(UserQueryRequest userLoginRequest);
}