package com.caixy.serviceclient.service.user;

import com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;

import java.util.Collection;
import java.util.List;

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
    UserOperatorResponse preRegister(EmailRegisterRequest emailRegisterRequest);

    /**
     * 激活用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午2:57
     */
    UserOperatorResponse activeUserAccount(User userInfo, String nickName, Integer userGender);


    UserQueryResponse<UserVO> query(UserQueryFacadeRequest userQueryFacadeRequest);

    UserQueryResponse<List<User>> queryUserByIds(Collection<Long> userIds);
}