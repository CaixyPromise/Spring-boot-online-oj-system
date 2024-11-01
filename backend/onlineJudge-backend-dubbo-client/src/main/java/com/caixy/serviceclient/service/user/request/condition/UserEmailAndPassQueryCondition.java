package com.caixy.serviceclient.service.user.request.condition;

import lombok.*;

/**
 * 用户查询的方法类型-邮箱和密码
 *
 * @name: com.caixy.serviceclient.service.user.request.condition.UserQueryCondition
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailAndPassQueryCondition implements UserQueryCondition
{

    private static final long serialVersionUID = 1L;

    /**
     * 用户手机号
     */
    private String email;

    /**
     * 密码
     */
    private String password;
}
