package com.caixy.onlineJudge.common.email.models.active;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送用户激活DTO
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.models.active.SendUserActiveDTO
 * @since 2024/9/6 下午3:56
 */
@Data
public class SendUserActiveDTO implements Serializable
{
    /**
     * 激活token
     */
    private String token;

    private static final long serialVersionUID = 1L;
}
