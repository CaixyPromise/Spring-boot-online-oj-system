package com.caixy.onlineJudge.common.email.models.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 发送邮件验证码载体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.models.captcha.SendCaptchaEmailDTO
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
