package com.caixy.onlineJudge.models.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员专用的用户信息VO
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.vo.user.UserAdminVO
 * @since 2024/9/10 上午2:50
 */
@Data
public class UserAdminVO implements Serializable
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
     * github用户Id
     */
    private Long githubId;

    /**
     * 用户角色：user/admin/ban
     */
    private UserRoleEnum userRole;

    /**
     * 微信开放平台id
     */
    private String unionId;

    /**
     * 用户手机号(后期允许拓展区号和国际号码）
     */
    private String userPhone;


    /**
     * 用户性别
     */
    private Integer userGender;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private UserStateEnum userActive;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
