package com.caixy.onlineJudge.models.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 */
@Data
public class UserUpdateSelfRequest implements Serializable
{

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户性别
     */
    private Integer userGender;


    private static final long serialVersionUID = 1L;
}