package com.caixy.onlineJudge.business.question;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.caixy.onlineJudge.business.question.mapper")
@SpringBootApplication(
        scanBasePackages = {
        "com.caixy.onlineJudge.business.question",
        "com.caixy.onlineJudge.common",
        "com.caixy.onlineJudge.web",
        "com.caixy.onlineJudge.rabbitmq",
})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
@EnableDiscoveryClient
public class OnlineJudgeBackendServiceQuestionApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeBackendServiceQuestionApplication.class, args);
    }

}
