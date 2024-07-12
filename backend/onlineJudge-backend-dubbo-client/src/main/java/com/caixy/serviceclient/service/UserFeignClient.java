package com.caixy.serviceclient.service;

import com.caixy.common.common.ErrorCode;
import com.caixy.common.constant.UserConstant;
import com.caixy.common.exception.BusinessException;
import models.entity.User;
import models.enums.user.UserRoleEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口远程调用类
 *
 * @name: com.caixy.serviceclient.service.UserFeignClient
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 **/
public interface UserFeignClient
{

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request)
    {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null)
        {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 可以考虑在这里做全局权限校验
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user)
    {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }
}