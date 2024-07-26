package com.caixy.onlineJudge.business.user.facade;

import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.serviceclient.service.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户服务调用实现类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.user_service.service.inner.InnerUserFeignClientImpl
 * @since 2024-07-11 23:27
 **/
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService
{
    @Resource
    private UserService userService;
}
