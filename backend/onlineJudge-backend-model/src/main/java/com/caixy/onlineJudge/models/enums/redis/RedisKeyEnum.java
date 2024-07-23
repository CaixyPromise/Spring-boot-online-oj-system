package com.caixy.onlineJudge.models.enums.redis;

import lombok.Getter;

/**
 * Redis缓存Key配置
 *
 * @Author COMPROMISE
 * @name com.caixy.oj.models.enums.redis.RedisKeyEnum
 * @since 2024/7/23 上午12:24
 */
@Getter
public enum RedisKeyEnum
{
    CATEGORY_PARENT_BY_KEY("category:parent:", -1L),

    /**
     * 验证码缓存，5分钟
     */
    CAPTCHA_CODE("captcha:", 60L * 5),
    ;

    private final String key;
    private final Long expire;

    RedisKeyEnum(String key, Long expire)
    {
        this.key = key;
        this.expire = expire;
    }

    public String generateKey(String... items)
    {
        if (items == null || items.length == 0)
        {
            return key;
        }
        return key.concat(String.join(":", items));
    }
}
