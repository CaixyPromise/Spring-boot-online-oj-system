package com.caixy.onlineJudge.common.cache.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * redis序列化构造器
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.RedisSerializerConfig
 * @since 2024-06-16 11:06
 **/
@Component
public class RedisSerializerConfig
{
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        // 使用 Jackson 作为序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        return template;
    }
}
