package com.caixy.onlineJudge.common.email.constant;

/**
 * 邮件常量
 *
 * @name: com.caixy.adminSystem.constant.EmailConstant
 * @author: CAIXYPROMISE
 * @since: 2024-01-10 19:07
 **/
public interface EmailConstant
{
    /**
     * 邮箱消息队列
     */
    String EMAIL_QUEUE = "emailCaptchaQueue";

    String EMAIL_DEAD_QUEUE = "X-DeadLetter-Email-Captcha-Queue";


    /**
     * 电子邮件html内容路径 resources目录下
     */
    String EMAIL_HTML_CONTENT_PATH = "email-template/captcha.html";

    String EMAIL_HTML_ACTIVE_PATH = "email-template/active-user.html";

    /**
     * 电子邮件html支付成功路径
     */
    String EMAIL_HTML_PAY_SUCCESS_PATH = "email-template/pay.html";

    /**
     * 修改邮箱请求captcha缓存键 校验之前的邮箱
     */
    String EMAIL_CAPTCHA_CACHE_KEY = "API:CAPTCHA_BEFORE:";
    /**
     * 修改邮箱请求captcha缓存有效时间 5 minute
     */
    Long EMAIL_CAPTCHA_CACHE_TTL = (5L * 60L);
    String EMAIL_NEW_CAPTCHA_CACHE_KEY = "API:CAPTCHA_AFTER:";

    String EMAIL_MODIFY_PASSWORD_CACHE_KEY = "USER:MODIFY_PASSWORD:";
    /**
     * 电子邮件主题
     */
    String EMAIL_SUBJECT = "验证码邮件";

    /**
     * 电子邮件标题
     */
    String EMAIL_TITLE = "PROMISE-API 接口开放平台";

    /**
     * 电子邮件标题英语
     */
    String EMAIL_TITLE_ENGLISH = "PROMISE-API Open Interface Platform";

    /**
     * 平台负责人
     */
    String PLATFORM_RESPONSIBLE_PERSON = "CAIXYPROMISE";

    /**
     * 平台地址
     */
    String PLATFORM_ADDRESS_A_HREF = "<a href='https://api.PROMISE.icu/%s'>%s</a>";

    String PATH_ADDRESS = "'https://api.PROMISE.icu/'";

}
