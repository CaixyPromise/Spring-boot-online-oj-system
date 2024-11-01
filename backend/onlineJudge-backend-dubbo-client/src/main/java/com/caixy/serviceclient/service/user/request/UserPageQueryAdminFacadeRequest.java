package com.caixy.serviceclient.service.user.request;

import com.caixy.onlineJudge.models.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户分页查询管理员操作
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.user.request.UserPageQueryAdminFacadeRequest
 * @since 2024/9/10 上午3:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageQueryAdminFacadeRequest extends PageRequest implements Serializable
{

    private static final long serialVersionUID = 1L;
}
