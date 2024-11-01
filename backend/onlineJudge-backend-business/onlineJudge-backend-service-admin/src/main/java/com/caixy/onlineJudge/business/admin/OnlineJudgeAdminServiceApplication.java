package com.caixy.onlineJudge.business.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 管理员接口服务
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.admin.OnlineJudgeAdminServiceApplication
 * @since 2024/9/10 上午12:56
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.caixy.onlineJudge.business.admin",
                "com.caixy.onlineJudge.common",
                "com.caixy.onlineJudge.web"
        },
        exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableDiscoveryClient
public class OnlineJudgeAdminServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeAdminServiceApplication.class, args);
    }
}
