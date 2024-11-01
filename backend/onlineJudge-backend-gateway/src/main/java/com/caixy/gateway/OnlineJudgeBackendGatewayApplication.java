package com.caixy.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
},
        scanBasePackages = {
                "com.caixy.gateway",
                "com.caixy.gateway.auth"
        })
@EnableDiscoveryClient
@ComponentScan("com.caixy.gateway.*")
@EnableDubbo
public class OnlineJudgeBackendGatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineJudgeBackendGatewayApplication.class, args);
    }
}
