package com.caixy.onlineJudge.web.config;

import com.caixy.onlineJudge.constants.common.CommonConstant;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // 禁用CSRF
            .authorizeRequests(authz -> authz
                    .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()  // 允许所有OPTIONS请求
                    .requestMatchers(request -> {
                        String forwardBy = request.getHeader("X-Forwarded-By");
                        return forwardBy != null && forwardBy.equals("Service-Gateway");  // 确保是网关转发的请求
                    }).permitAll()
                    .anyRequest().denyAll()  // 拒绝所有其他请求
            );
        return http.build();
    }
}
