package com.caixy.onlineJudge.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Knife4jConfiguration 配置文件
 *
 * @author CAIXYPROMISE
 * @name com.caixy.user_service.config.Knife4jConfiguration
 * @since 2024-07-12 19:15
 **/
//@Configuration
public class Knife4jConfiguration
{
//    @Value("${knife4j.name}")
//    private String name;
//
//    @Value("${knife4j.description}")
//    private String description;
//
//    @Value("${knife4j.basePackage}")
//    private String basePackage;
//
//    @Bean
//    public Docket createRestApi()
//    {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(basePackage))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo()
//    {
//        return new ApiInfoBuilder()
//                .title(name)
//                .description(description)
//                .version("1.0")
//                .build();
//    }
}