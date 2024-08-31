package com.caixy.onlineJudge.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.caixy.onlineJudge.models.enums.user.UserGenderEnum;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable
{
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 密码
     */
    private String userPassword;

    /**
     * github用户Id
     */
    private Long githubId;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 微信开放平台id
     */
    private String unionId;

    /**
     * 用户手机号(后期允许拓展区号和国际号码）
     */
    private String userPhone;

    /**
     * 公众号openId
     */
    private String mpOpenId;

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
     * 是否激活
     */
    private Integer isActive;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static User registerUserByEmail(
            String nickName,
            String userEmail,
            String userPassword)
    {
        return User.builder()
                .nickName(nickName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userGender(UserGenderEnum.UNKNOWN.getValue())
                .userRole(UserRoleEnum.USER.getValue())
                .isActive(UserStateEnum.ACTIVE.getCode())
                .isDelete(0)
                .build();
    }
}