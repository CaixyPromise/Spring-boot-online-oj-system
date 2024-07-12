package com.caixy.common.constant;

import lombok.Getter;

/**
 * @Name: com.caixy.project.constant.RedisKeyEnum
 * @Description: Redis缓存的常量：Key和过期时间
 * @Author: CAIXYPROMISE
 * @Date: 2023-12-20 20:20
 **/
@Getter
public enum RedisKeyEnum
{
    CATEGORY_PARENT_BY_KEY("category:parent:", 3600L * 24 * 7),

    ;
    private final String key;
    private final Long expire;

    RedisKeyEnum(String key, Long expire)
    {
        this.key = key;
        this.expire = expire;
    }

    public String generateKey(String... values)
    {
        String joinValue = String.join(":", values);
        if (this.key.charAt(this.key.length() - 1) == ':')
        {
            return this.key + joinValue;
        }
        return this.key + ":" + joinValue;
    }
}
