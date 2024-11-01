package com.caixy.onlineJudge.business.question.config;

import com.caixy.serviceclient.service.user.UserAdminFacadeService;
import com.caixy.serviceclient.service.user.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动引入dubbo服务
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.question.config.QuetionDubboConfig
 * @since 2024/9/11 上午1:01
 */
@Configuration
public class QuestionDubboConfig
{
    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private UserAdminFacadeService userAdminFacadeService;

    @Bean
    @ConditionalOnMissingBean
    public UserFacadeService userFacadeService()
    {
        return userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean
    public UserAdminFacadeService userAdminFacadeService()
    {
        return userAdminFacadeService;
    }
}