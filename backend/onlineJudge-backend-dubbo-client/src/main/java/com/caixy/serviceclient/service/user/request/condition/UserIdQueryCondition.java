package com.caixy.serviceclient.service.user.request.condition;

import lombok.*;

/**
 * 用户查询的方法类型-id查询
 *
 * @name: com.caixy.serviceclient.service.user.request.condition.UserIdQueryCondition
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
}
