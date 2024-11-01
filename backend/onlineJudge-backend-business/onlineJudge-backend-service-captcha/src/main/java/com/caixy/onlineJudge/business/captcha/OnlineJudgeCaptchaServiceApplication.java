package com.caixy.onlineJudge.business.captcha;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 验证码服务类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.OnlineJudgeCaptchaServiceApplication
 * @since 2024/7/30 上午1:01
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.caixy.onlineJudge.business.captcha",
                "com.caixy.onlineJudge.common",
                "com.caixy.onlineJudge.web",
                "com.caixy.onlineJudge.rabbitmq",
        },
        exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableDiscoveryClient
public class OnlineJudgeCaptchaServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeCaptchaServiceApplication.class, args);
    }
}
