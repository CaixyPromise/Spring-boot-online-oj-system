package com.caixy.onlineJudge.business.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.common.UserConstant;
import com.caixy.onlineJudge.models.common.DeleteRequest;
import com.caixy.onlineJudge.models.dto.user.*;
import com.caixy.onlineJudge.business.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.AboutMeVO;
import com.caixy.onlineJudge.models.vo.user.LoginUserVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController
{
    @Resource
    private UserService userService;

    // region 登录相关
    /**
     * 获取当前登录用户
     *
     * @param request
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request)
    {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 管理员增删改查
    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request)
    {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        userService.validUserInfo(user, false);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    // endregion

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request)
    {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody
                                                   UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request)
    {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody
                                                       UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request)
    {
        if (userQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion


    @GetMapping("/get/me")
    public BaseResponse<AboutMeVO> getMe(HttpServletRequest request)
    {
        User loginUser = userService.getLoginUser(request);
        User currentUser = userService.getById(loginUser.getId());

        return ResultUtils.success(AboutMeVO.of(currentUser));
    }

    @PostMapping("/modify/password")
    public BaseResponse<Boolean> modifyPassword(@RequestBody
                                                UserModifyPasswordRequest userModifyPasswordRequest,
                                                HttpServletRequest request)
    {
        if (userModifyPasswordRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Boolean result = userService.modifyPassword(loginUser.getId(), userModifyPasswordRequest);
        // 如果修改成功，修改登录状态
        if (result)
        {
            request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        }
        return ResultUtils.success(result);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request)
    {
        if (userUpdateMyRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        userService.validUserInfo(user, false);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
