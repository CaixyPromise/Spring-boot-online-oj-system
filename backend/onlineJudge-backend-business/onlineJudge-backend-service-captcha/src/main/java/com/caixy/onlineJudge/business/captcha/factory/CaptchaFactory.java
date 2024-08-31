package com.caixy.onlineJudge.business.captcha.factory;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.caixy.onlineJudge.business.captcha.annotation.CaptchaTypeTarget;
import com.caixy.onlineJudge.business.captcha.strategy.CaptchaGenerationStrategy;
import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.base.utils.SpringContextUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.CommonConstants;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码生成工厂
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.factory.CaptchaFactory
 * @since 2024-07-16 03:33
 **/
@Component
@Slf4j
public class CaptchaFactory
{
    @Resource
    private List<CaptchaGenerationStrategy> captchaGenerationStrategies;

    @Resource
    private RedisUtils redisUtils;

    private ConcurrentHashMap<String, CaptchaGenerationStrategy> serviceCache;

    private List<CaptchaGenerationStrategy> registeredStrategies;

    @PostConstruct
    public void initActionService()
    {
        serviceCache =
                SpringContextUtils.getServiceFromAnnotation(captchaGenerationStrategies,
                        CaptchaTypeTarget.class,
                        "value");
        registeredStrategies = new ArrayList<>(serviceCache.values());
    }

    public CaptchaGenerationStrategy getCaptchaStrategy(String type)
    {
        return serviceCache.get(type);
    }

    public CaptchaGenerationStrategy getRandomCaptchaStrategy()
    {
        return RandomUtil.randomEle(registeredStrategies);
    }

    public boolean verifyCaptcha(String captchaCode)
    {
        // 获取当前用户的验证码Id
        String codeId = SaManager.getSaTokenDao().get(CommonConstants.CAPTCHA_SIGN);
        if (StringUtils.isBlank(codeId))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 1.2 校验验证码
        String redisCode = redisUtils.getString(RedisKeyEnum.CAPTCHA_CODE, codeId);
        if (StringUtils.isBlank(redisCode))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 移除缓存的uuid
        SaManager.getSaTokenDao().delete(CommonConstants.CAPTCHA_SIGN);
        boolean removeByCache = redisUtils.delete(RedisKeyEnum.CAPTCHA_CODE, codeId);
        if (!removeByCache)
        {
            log.warn("验证码校验失败，移除缓存失败，sessionId:{}", codeId);
        }
        // 验证码不区分大小写
        return !redisCode.equalsIgnoreCase(captchaCode.trim());
    }
}
