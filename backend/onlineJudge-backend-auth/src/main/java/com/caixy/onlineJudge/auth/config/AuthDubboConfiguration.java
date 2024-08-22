package com.caixy.onlineJudge.auth.config;

import com.caixy.serviceclient.service.user.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 权限校验服务的Dubbo远程调用配置类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.config.AuthDubboConfiguration
 * @since 2024/8/1 上午1:56
 */
//@Configuration
public class AuthDubboConfiguration
{
    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @Bean
    @ConditionalOnMissingBean
    public UserFacadeService userFacadeService()
    {
        return userFacadeService;
    }
}
