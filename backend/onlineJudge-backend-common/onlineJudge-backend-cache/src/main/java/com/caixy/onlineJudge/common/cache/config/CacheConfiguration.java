package com.caixy.onlineJudge.common.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置，自动启动jetcache方法缓存
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.cache.config.CacheConfiguration
 * @since 2024/9/10 上午3:35
 */
@Configuration
@EnableMethodCache(basePackages = "com.caixy.onlineJudge")
public class CacheConfiguration
{
}
