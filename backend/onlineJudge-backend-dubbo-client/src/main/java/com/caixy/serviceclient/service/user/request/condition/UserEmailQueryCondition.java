package com.caixy.serviceclient.service.user.request.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询用户邮箱请求方式
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.user.request.condition.UserEmailQueryCondition
 * @since 2024/9/10 上午2:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailQueryCondition implements UserQueryCondition
{
    private String email;

    private static final long serialVersionUID = 1L;
}
