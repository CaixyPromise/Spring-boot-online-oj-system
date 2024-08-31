package com.caixy.onlineJudge.models.dto.captcha;

import lombok.Data;

import java.io.Serializable;

/**
 * email发送请求DTO
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.captcha.EmailSenderCaptchaRequest
 * @since 2024/8/27 上午2:44
 */
@Data
public class EmailSenderCaptchaRequest implements Serializable
{
    /**
     * 发送目标邮箱
     */
    private String toEmail;

    /**
     * 发送类型
     */
    private Integer type;
    private static final long serialVersionUID = 1L;
}
