package com.caixy.onlineJudge.common.cache.redis.annotation;


import com.caixy.onlineJudge.models.enums.redis.RedisLimiterEnum;

import java.lang.annotation.*;

/**
 * @name: com.caixy.adminSystem.annotation.RateLimitFlow
 * @description: 限流器注解
 * @author: CAIXYPROMISE
 * @date: 2024-07-17 03:12
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimitFlow
{
    RedisLimiterEnum key();
    String args() default "";
    String errorMessage() default "";
}
