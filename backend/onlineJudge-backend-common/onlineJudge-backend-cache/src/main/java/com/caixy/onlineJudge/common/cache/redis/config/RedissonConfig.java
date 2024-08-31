package com.caixy.onlineJudge.common.cache.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * RedissonClient配置类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.cache.redis.config.RedissonConfig
 * @since 2024/9/1 上午12:06
 */
@Configuration
public class RedissonConfig
{
    @Value("${spring.redis.redisson.config}")
    private String redissonConfig;

    @Bean
    public RedissonClient redissonClient() throws IOException
    {
        // 解析 YAML 格式的配置
        Config config = Config.fromYAML(redissonConfig);
        return Redisson.create(config);
    }
}
