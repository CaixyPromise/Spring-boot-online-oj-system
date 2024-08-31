package com.caixy.onlineJudge.common.email.aspect;

import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 发送邮件处理者
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.aspect.EmailCategoryHandler
 * @since 2024/8/24 上午1:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EmailCategoryHandler
{
    EmailSenderCategoryEnum value();
}
