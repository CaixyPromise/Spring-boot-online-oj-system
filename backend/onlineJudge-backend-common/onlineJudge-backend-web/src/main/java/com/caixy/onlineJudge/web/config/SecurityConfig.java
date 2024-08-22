package com.caixy.onlineJudge.web.config;

import com.caixy.onlineJudge.constants.common.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.user_service.config.SecurityConfig
 * @since 2024-07-12 02:10
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable) // 使用新的配置方式禁用CSRF保护
                .authorizeRequests(authz -> authz
                        .requestMatchers(request -> {
                            String forwardBy = request.getHeader("X-Forwarded-By");
                            return StringUtils.isNotBlank(forwardBy) && forwardBy.equals(CommonConstants.HEADER_SERVICE_GATEWAY_VALUE);
                        }).permitAll()  // 只允许网关转发的请求
                        .anyRequest().permitAll()//.denyAll()  // 拒绝所有其他请求
                );;
        return http.build();
    }
}
