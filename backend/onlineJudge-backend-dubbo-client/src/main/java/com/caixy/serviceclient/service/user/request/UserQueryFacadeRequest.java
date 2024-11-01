package com.caixy.serviceclient.service.user.request;

import com.caixy.serviceclient.service.user.request.condition.UserQueryCondition;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询用户的请求
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.user.request.UserQueryRequest
 * @since 2024/9/10 上午2:00
 */
@Data
@Builder
public class UserQueryFacadeRequest implements Serializable
{
    private UserQueryCondition userQueryCondition;

    public static UserQueryFacadeRequest build(UserQueryCondition userQueryCondition)
    {
        return UserQueryFacadeRequest.builder().userQueryCondition(userQueryCondition).build();
    }

    private static final long alVersionUID = 1L;
}
