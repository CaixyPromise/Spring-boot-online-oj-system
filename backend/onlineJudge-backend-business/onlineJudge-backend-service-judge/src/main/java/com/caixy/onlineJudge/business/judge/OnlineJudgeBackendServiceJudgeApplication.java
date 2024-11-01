package com.caixy.onlineJudge.business.judge;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(
        scanBasePackages = {
                "com.caixy.onlineJudge.business.judge",
                "com.caixy.onlineJudge.common",
                "com.caixy.onlineJudge.web",
                "com.caixy.onlineJudge.rabbitmq",
        },
        exclude = {
                DataSourceAutoConfiguration.class
        })
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
@EnableDiscoveryClient
public class OnlineJudgeBackendServiceJudgeApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeBackendServiceJudgeApplication.class, args);
    }

}
