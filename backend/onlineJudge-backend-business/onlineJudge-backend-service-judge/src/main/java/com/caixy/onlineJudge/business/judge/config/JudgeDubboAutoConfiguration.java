package com.caixy.onlineJudge.business.judge.config;

import com.caixy.serviceclient.service.question.QuestionFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 判题服务自动引入dubbo服务类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.config.JudgeDubboAutoConfiguration
 * @since 2024/9/11 下午4:33
 */
@Configuration
public class JudgeDubboAutoConfiguration
{
    @DubboReference(version = "1.0.0")
    private QuestionFacadeService questionFacadeService;

    @Bean
    @ConditionalOnMissingBean
    public QuestionFacadeService questionFacadeService()
    {
        return questionFacadeService;
    }
}
