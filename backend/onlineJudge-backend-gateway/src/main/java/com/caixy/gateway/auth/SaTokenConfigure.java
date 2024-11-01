package com.caixy.gateway.auth;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.caixy.onlineJudge.models.enums.user.UserPermission;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * sa-token的全局配置
 *
 * @author Hollis
 */
@Configuration
@Slf4j
public class SaTokenConfigure
{
    @Resource
    private Environment environment;

    @Bean
    public SaReactorFilter getSaReactorFilter()
    {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 开放地址
                .addExclude("/favicon.ico")

                // 鉴权方法：每次访问进入
                .setAuth(obj ->
                {
                    SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
                    // 登录校验 -- 拦截所有路由，并排除/auth/login 用于开放登录
                    SaRouter.match("/**")
//                            .notMatch("/api/**")//系统服务全排除
                            // 下边的是knife4j使用的
                            .notMatch("/*/*.html") // 仅匹配一级目录下的HTML文件
                            .notMatch("/swagger-resources/**")
                            .notMatch("/webjars/**")
                            .notMatch("/*/api-docs") // 仅匹配一级目录下的api-docs
                            // 排除登录接口
                            .notMatch("/api/auth/**")
                            // 排除文档接口
                            .notMatch("/api/*/v2/api-docs")
                            // 排除验证码
                            .notMatch("/api/captcha/*/**")
                            // 权限认证 -- 不同模块, 校验不同权限
//                            .match("/api/admin/**", r -> StpUtil.checkPermission(UserPermission.AUTH.name()))
                            .match("/api/user/**", r -> StpUtil.checkPermission(UserPermission.BASIC.name()));
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaRequest request = SaHolder.getRequest();

                    // 设置CORS响应头
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
                            .setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                            .setHeader("Access-Control-Allow-Headers", "Content-Type, authToken, Authorization")
                            .setHeader("Access-Control-Allow-Credentials", "true")  // 允许携带Cookie
                            .setHeader("Access-Control-Max-Age", "3600");  // 预检请求缓存时间

                    // 如果是OPTIONS预检请求，则直接返回
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(this::getSaResult);
    }

    private SaResult getSaResult(Throwable throwable)
    {
        if (throwable instanceof NotLoginException)
        {
            log.error("请先登录");
            return SaResult.error("请先登录");
        }
        else if (throwable instanceof NotRoleException)
        {
            NotRoleException notRoleException = (NotRoleException) throwable;
            if (UserRoleEnum.ADMIN.name().equals(notRoleException.getRole()))
            {
                log.error("请勿越权使用！");
                return SaResult.error("请勿越权使用！");
            }
            log.error("您无权限进行此操作！");
            return SaResult.error("您无权限进行此操作！");
        }
        else if (throwable instanceof NotPermissionException)
        {
            NotPermissionException notPermissionException = (NotPermissionException) throwable;
            if (UserPermission.AUTH.name().equals(notPermissionException.getPermission()))
            {
                log.error("请先完成实名认证！");
                return SaResult.error("请先完成实名认证！");
            }
            log.error("您无权限进行此操作！");
            return SaResult.error("您无权限进行此操作！");
        }
        else
        {
            return SaResult.error(throwable.getMessage());
        }
    }

}
