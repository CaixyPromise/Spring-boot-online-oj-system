package com.caixy.onlineJudge.business.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;


@MapperScan("com.caixy.onlineJudge.business.user.mapper")
@EnableScheduling
@SpringBootApplication(
        scanBasePackages = {
                "com.caixy.onlineJudge.business.user",
                "com.caixy.onlineJudge.common",
                "com.caixy.onlineJudge.web"
        }
)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
@EnableDiscoveryClient
public class OnlineJudgeUserServiceApplication
{
    public static void main(String[] args)
    {
        // 创建SpringApplication实例
        SpringApplication application = new SpringApplication(OnlineJudgeUserServiceApplication.class);
        // 添加自定义的ApplicationContextInitializer
        application.addInitializers(context ->
        {
            // 获取Environment对象
            Environment env = context.getEnvironment();
            // 从Environment中读取"spring.application.name"属性值
            String appName = env.getProperty("spring.application.name");
            String filePath = System.getProperty("user.home") + File.separator + ".dubbo" + File.separator + appName;
            // 修改dubbo的本地缓存路径，避免缓存冲突
            System.setProperty("dubbo.meta.cache.filePath", filePath);
            System.setProperty("dubbo.mapping.cache.filePath", filePath);
        });
        //启动应用
        application.run(args);

    }
}
