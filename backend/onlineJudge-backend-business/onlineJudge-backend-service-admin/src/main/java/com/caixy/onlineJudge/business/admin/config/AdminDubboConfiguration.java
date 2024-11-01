package com.caixy.onlineJudge.business.admin.config;

import com.caixy.serviceclient.service.question.QuestionFacadeService;
import com.caixy.serviceclient.service.user.UserAdminFacadeService;
import com.caixy.serviceclient.service.user.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Admin自动导入Dubbo资源
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.admin.config.AdminDubboConfiguration
 * @since 2024/9/10 上午3:32
 */
@Configuration
public class AdminDubboConfiguration
{
    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;


    @DubboReference(version = "1.0.0")
    private UserAdminFacadeService userAdminFacadeService;

    @DubboReference(version = "1.0.0")
    private QuestionFacadeService questionFacadeService;

    @Bean
    @ConditionalOnMissingBean
    public UserAdminFacadeService userAdminFacadeService()
    {
        return userAdminFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean
    public UserFacadeService userFacadeService()
    {
        return userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean
    public QuestionFacadeService questionFacadeService()
    {
        return questionFacadeService;
    }
}
