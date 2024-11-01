package com.caixy.serviceclient.service.user.request.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询的方法类型-用户名
 *
 * @name: com.caixy.serviceclient.service.user.request.condition.UserNickNameQueryCondition
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNickNameQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String nickName;
}
