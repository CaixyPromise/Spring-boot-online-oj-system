package com.caixy.onlineJudge.business.captcha.annotation;

import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;

import java.lang.annotation.*;

/**
 * Email类型Target注解
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.annotation.EmailTypeTarget
 * @since 2024/8/27 下午5:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EmailTypeTarget
{
    /**
     * 类型值
     */
    EmailSenderCategoryEnum[] value();
}
