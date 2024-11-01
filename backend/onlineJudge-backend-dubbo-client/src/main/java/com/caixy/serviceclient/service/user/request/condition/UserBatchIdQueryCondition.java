package com.caixy.serviceclient.service.user.request.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 批量查询Id
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.user.request.condition.UserBatchIdQueryCondition
 * @since 2024/9/11 上午1:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBatchIdQueryCondition implements UserQueryCondition
{
    private Collection<Long> userIds;
}
