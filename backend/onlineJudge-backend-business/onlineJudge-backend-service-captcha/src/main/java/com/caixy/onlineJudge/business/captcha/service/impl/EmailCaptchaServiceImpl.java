package com.caixy.onlineJudge.business.captcha.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.caixy.onlineJudge.business.captcha.annotation.EmailTypeTarget;
import com.caixy.onlineJudge.common.email.models.captcha.SendCaptchaEmailDTO;
import com.caixy.onlineJudge.common.email.mq.EmailSenderRabbitMQProducer;
import com.caixy.onlineJudge.business.captcha.service.EmailCaptchaService;
import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.common.regex.RegexUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮箱验证码服务实现类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.service.impl.EmailCaptchaServiceImpl
 * @since 2024/8/27 下午5:27
 */
@Service
public class EmailCaptchaServiceImpl implements EmailCaptchaService
{
    private static final String EMAIL_CAPTCHA_KEY = "emailCaptcha";
    private static final String EMAIL_CAPTCHA_TIME_KEY = "timestamp";
    private static final String EMAIL_CAPTCHA_SCENE_KEY = "scene";
    private static final Logger log = LoggerFactory.getLogger(EmailCaptchaServiceImpl.class);
    @Resource
    private EmailSenderRabbitMQProducer<SendCaptchaEmailDTO> emailCaptchaRabbitMQProducer;
    @Resource
    private RedisUtils redisUtils;
    private ConcurrentHashMap<EmailSenderCategoryEnum, Method> emailSenderMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init()
    {
        fillEmailSenderMap();
    }

    private void fillEmailSenderMap()
    {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(EmailTypeTarget.class))
            {
                EmailTypeTarget emailTypeTarget = method.getAnnotation(EmailTypeTarget.class);
                EmailSenderCategoryEnum[] value = emailTypeTarget.value();
                for (EmailSenderCategoryEnum emailSenderCategoryEnum : value)
                {
                    emailSenderMap.put(emailSenderCategoryEnum, method);
                }
            }
        }
        log.info("map: {}", emailSenderMap);
    }

    /**
     * 验证邮箱验证码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/8/29 下午2:57
     */
    @Override
    public Boolean verifyEmailCaptcha(String toEmail, String captcha, Integer scene)
    {
        Map<String, Object> emailCacheMap = redisUtils.getHashMap(RedisKeyEnum.EMAIL_CAPTCHA, String.class,
                Object.class, toEmail);
        String code = Optional.ofNullable(emailCacheMap.get(EMAIL_CAPTCHA_KEY))
                              .orElseThrow(
                                      () -> new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误")).toString();
        String timestamp = Optional.ofNullable(emailCacheMap.get(EMAIL_CAPTCHA_TIME_KEY))
                                   .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR,
                                           "验证码错误")).toString();
        Integer cacheScene = Integer.parseInt(Optional.ofNullable(emailCacheMap.get(EMAIL_CAPTCHA_SCENE_KEY))
                                                      .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR,
                                                              "验证码错误")).toString());
        EmailSenderCategoryEnum categoryEnum = EmailSenderCategoryEnum.getEnumByCode(cacheScene);
        EmailSenderCategoryEnum senderCategoryEnum = EmailSenderCategoryEnum.getEnumByCode(scene);

        redisUtils.delete(RedisKeyEnum.EMAIL_CAPTCHA, toEmail);

        return captcha.equals(code) &&
               Long.parseLong(timestamp) + 5 * 60 * 1000 > System.currentTimeMillis() &&
               categoryEnum.equals(senderCategoryEnum);
    }

    /**
     * 发送邮件
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/8/29 下午2:57
     */
    @Override
    public Boolean doSend(String toEmail, EmailSenderCategoryEnum emailSenderCategoryEnum)
    {
        try
        {
            Method method = emailSenderMap.get(emailSenderCategoryEnum);
            if (method == null)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "发送类型错误");
            }
            else
            {
                method.invoke(this, toEmail, emailSenderCategoryEnum);
            }
            return true;
        }
        catch (InvocationTargetException |
               IllegalAccessException e)
        {
            log.info(e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送失败");
        }
    }

    @EmailTypeTarget(value = {EmailSenderCategoryEnum.CAPTCHA, EmailSenderCategoryEnum.REGISTER})
    protected Boolean sendEmailCaptcha(String toEmail, EmailSenderCategoryEnum senderCategoryEnum)
    {
        ThrowUtils.throwIf(!RegexUtils.isEmail(toEmail), ErrorCode.PARAMS_ERROR, "收件人邮箱格式错误");

        Map<String, Object> hashMap = redisUtils.getHashMap(RedisKeyEnum.EMAIL_CAPTCHA, String.class, Object.class,
                toEmail);
        if (hashMap != null && hashMap.get(EMAIL_CAPTCHA_TIME_KEY) != null)
        {
            long timestamp = (long) hashMap.get(EMAIL_CAPTCHA_TIME_KEY);
            if (System.currentTimeMillis() - timestamp < 60 * 1000)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码发送过于频繁，请稍后再试");
            }
        }
        String captchaCode = RandomUtil.randomNumbers(6);
        Map<String, Object> captchaCodeMap = new HashMap<>();
        captchaCodeMap.put(EMAIL_CAPTCHA_KEY, captchaCode);
        captchaCodeMap.put(EMAIL_CAPTCHA_TIME_KEY, System.currentTimeMillis());
        captchaCodeMap.put(EMAIL_CAPTCHA_SCENE_KEY, senderCategoryEnum.getCode());
        redisUtils.setHashMap(RedisKeyEnum.EMAIL_CAPTCHA, captchaCodeMap, toEmail);
        emailCaptchaRabbitMQProducer.sendEmail(toEmail,
                new SendCaptchaEmailDTO(captchaCode),
                EmailSenderCategoryEnum.CAPTCHA);
        return true;
    }

}
