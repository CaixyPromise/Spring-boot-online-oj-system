package com.caixy.onlineJudge.models.vo.user;

import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 */
@Data
public class UserVO implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/ban
     */
    private UserRoleEnum userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private UserStateEnum userActive;

    private static final long serialVersionUID = 1L;
}