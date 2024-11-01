package com.caixy.gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import com.caixy.gateway.auth.StpInterfaceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa token配置累
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.gateway.config.SaTokenConfigure
 * @since 2024/9/13 下午3:56
 */
@Configuration
public class AuthSaTokenConfigure
{
    @Bean
    public StpInterface stpInterface() {
        return new StpInterfaceImpl(); // 显式注入自定义的 StpInterface 实现
    }
}
