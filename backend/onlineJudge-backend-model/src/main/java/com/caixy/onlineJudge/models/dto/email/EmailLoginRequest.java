package com.caixy.onlineJudge.models.dto.email;

import lombok.Data;

import java.io.Serializable;

/**
 * Email登录注册请求
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.email.EmailLoginRequest
 * @since 2024/8/29 下午5:57
 */
@Data
public class EmailLoginRequest implements Serializable
{
    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;
    private static final long serialVersionUID = 1L;
}
