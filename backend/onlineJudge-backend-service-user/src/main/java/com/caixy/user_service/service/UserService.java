package com.caixy.user_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import models.dto.user.UserLoginRequest;
import models.dto.user.UserModifyPasswordRequest;
import models.dto.user.UserQueryRequest;
import models.dto.user.UserRegisterRequest;
import models.entity.User;
import models.vo.user.LoginUserVO;
import models.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author CAIXYPROMISE
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-07-11 23:46:33
 */
public interface UserService extends IService<User>
{
    /**
     * 用户注册
     *
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户请求信息
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    //    Long makeRegister(String userAccount, String userPassword);
    Long makeRegister(User user);

    String generatePassword();

    Boolean modifyPassword(Long userId, UserModifyPasswordRequest userModifyPasswordRequest);

    void validUserInfo(User user, boolean add);

    Map<Long, String> getUserNameByIds(Collection<Long> ids);
}
