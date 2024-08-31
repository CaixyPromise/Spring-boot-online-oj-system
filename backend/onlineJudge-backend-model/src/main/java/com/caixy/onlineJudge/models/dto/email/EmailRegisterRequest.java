package com.caixy.onlineJudge.models.dto.email;

import lombok.Data;

import java.io.Serializable;

/**
 * Email注册请求
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.email.EmailRegisterRequest
 * @since 2024/8/31 下午3:12
 */
@Data
public class EmailRegisterRequest implements Serializable
{
    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;
    private static final long serialVersionUID = 1L;
}
