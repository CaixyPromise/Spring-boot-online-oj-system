package com.caixy.serviceclient.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.models.vo.user.UserAdminVO;
import com.caixy.serviceclient.service.user.request.UserPageQueryAdminFacadeRequest;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;

/**
 * 管理员用户操作接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.user.UserAdminFacadeService
 * @since 2024/9/10 上午2:52
 */

public interface UserAdminFacadeService
{
    UserOperatorResponse deleteUserById(Long id);

    UserQueryResponse<UserAdminVO> query(UserQueryFacadeRequest userQueryFacadeRequest);

    UserQueryResponse<Page<UserAdminVO>> page(UserPageQueryAdminFacadeRequest pageQueryAdminFacadeRequest);
}
