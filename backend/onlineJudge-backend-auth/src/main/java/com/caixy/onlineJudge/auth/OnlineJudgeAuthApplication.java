package com.caixy.onlineJudge.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;


/**
 * 鉴权中心
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.OnlineJudgeAuthApplication
 * @since 2024/7/25 下午8:34
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.caixy.onlineJudge.auth",
                "com.caixy.onlineJudge.common"},
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableDubbo
public class OnlineJudgeAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeAuthApplication.class, args);
    }
}
