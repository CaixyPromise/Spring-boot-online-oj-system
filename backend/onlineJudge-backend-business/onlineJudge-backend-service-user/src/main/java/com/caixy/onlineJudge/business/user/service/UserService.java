package com.caixy.onlineJudge.business.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.onlineJudge.common.cache.redis.annotation.DistributedLock;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.user.*;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.redis.RDLockKeyEnum;
import com.caixy.onlineJudge.models.vo.user.LoginUserVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CAIXYPROMISE
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-07-11 23:46:33
 */
public interface UserService extends IService<User>
{

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

    String generatePassword();

    Boolean modifyPassword(Long userId, UserModifyPasswordRequest userModifyPasswordRequest);

    @DistributedLock(lockKeyEnum = RDLockKeyEnum.USER_LOCK, args = "#email")
    UserOperatorResponse registerByEmail(String email, String password);

    UserOperatorResponse doAuthLogin(OAuthResultDTO oAuthResultDTO);

    void validUserInfo(User user, boolean isAdd);

    UserVO findByEmail(String email);

    boolean emailExist(String email);

    boolean nickNameExist(String nickName);

    UserVO findByEmailAndPass(String email, String password);
}
