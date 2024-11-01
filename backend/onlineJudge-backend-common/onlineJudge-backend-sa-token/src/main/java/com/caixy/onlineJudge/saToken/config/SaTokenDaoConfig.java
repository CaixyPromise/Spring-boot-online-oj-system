package com.caixy.onlineJudge.saToken.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoRedisJackson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SaTokenDao配置Redis
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.web.config.SaTokenDaoConfig
 * @since 2024/9/1 上午1:47
 */
@Configuration
public class SaTokenDaoConfig
{
    @Bean
    public SaTokenDao saTokenDao()
    {
        // 使用 Redis 存储器，并且使用 Jackson 序列化
        return new SaTokenDaoRedisJackson();
    }
}
