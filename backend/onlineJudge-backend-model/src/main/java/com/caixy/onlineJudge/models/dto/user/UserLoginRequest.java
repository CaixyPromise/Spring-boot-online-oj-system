package com.caixy.onlineJudge.models.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable
{

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String captcha;

    private String captchaId;

    private String userPassword;
}
