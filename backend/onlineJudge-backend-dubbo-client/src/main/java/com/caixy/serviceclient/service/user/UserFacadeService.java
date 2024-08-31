package com.caixy.serviceclient.service.user;

import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
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
     * 用户注册
     *
     * @param emailRegisterRequest
     * @return
     */
    UserOperatorResponse register(EmailRegisterRequest emailRegisterRequest);

    /**
     * 用户信息查询
     *
     * @param email@return
     */
    UserQueryResponse<UserVO> queryByEmail(String email);

    UserQueryResponse<UserVO> queryByEmailAndPass(String email, String password);
}