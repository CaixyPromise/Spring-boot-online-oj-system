package com.caixy.onlineJudge.auth.annotation;

import com.caixy.onlineJudge.models.enums.oauth2.OAuthProviderEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入OAuth2配置客户端配置
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.annotation.InjectOAuthConfig
 * @since 2024/8/6 上午1:32
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectOAuthConfig
{
    OAuthProviderEnum clientName();
    String name() default "";
}
