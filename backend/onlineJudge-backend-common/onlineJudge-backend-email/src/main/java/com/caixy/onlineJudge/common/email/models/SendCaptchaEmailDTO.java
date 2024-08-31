package com.caixy.onlineJudge.common.email.models;

import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 发送邮件验证码载体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.models.SendCaptchaEmailDTO
 * @since 2024/8/24 下午2:04
 */
@Data
@AllArgsConstructor
public class SendCaptchaEmailDTO implements Serializable
{
    /**
     * 验证码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}
