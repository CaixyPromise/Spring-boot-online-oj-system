package com.caixy.onlineJudge.business.captcha.service;

import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;

/**
 * 邮箱验证码服务
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.service.EmailCaptchaSerivce
 * @since 2024/8/27 下午5:26
 */
public interface EmailCaptchaService
{
    Boolean verifyEmailCaptcha(String toEmail, String captcha, Integer type);

    Boolean doSend(String toEmail, EmailSenderCategoryEnum emailSenderCategoryEnum);
}
