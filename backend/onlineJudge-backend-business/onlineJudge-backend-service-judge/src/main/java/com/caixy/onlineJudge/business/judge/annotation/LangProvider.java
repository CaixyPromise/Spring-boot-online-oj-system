package com.caixy.onlineJudge.business.judge.annotation;

import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 沙箱标记注解
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.annotation.SandBoxProvider
 * @since 2024/9/11 上午2:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LangProvider
{
    LanguageProviderEnum value();
}
