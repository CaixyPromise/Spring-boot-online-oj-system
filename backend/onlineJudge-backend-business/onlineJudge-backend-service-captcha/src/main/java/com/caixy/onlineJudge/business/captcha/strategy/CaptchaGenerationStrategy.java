package com.caixy.onlineJudge.business.captcha.strategy;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;

import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;
import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.CommonConstants;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * 验证码生成抽象类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.strategy.CaptchaGenerationStrategy
 * @since 2024-07-15 19:43
 **/
@Slf4j
public abstract class CaptchaGenerationStrategy
{
    @Resource
    private RedisUtils redisUtils;

    protected Producer captchaProducer;

    @PostConstruct
    public void init()
    {
        captchaProducer = makeProducer();
    }

    public abstract CaptchaVO generateCaptcha(HttpServletResponse response);

    protected abstract Producer makeProducer(); // 确保这个方法只能被继承者使用

    protected CaptchaVO saveResult(String code, BufferedImage image)
    {
        // 以uuid作为凭证，
        String uuid = UUID.randomUUID().toString();

        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", outputStream);
        }
        catch (IOException e)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 写入redis
        // 并设置过期时间: 5分钟
        log.info("captcha code: {}", code);
        SaManager.getSaTokenDao().set(CommonConstants.CAPTCHA_SIGN, uuid, 5 * 60);
        redisUtils.setString(RedisKeyEnum.CAPTCHA_CODE,
                code, uuid);
        // 过期时间5分钟
        // 返回Base64的验证码图片信息
        return CaptchaVO.builder()
                        .codeImage(Base64.encode(outputStream.toByteArray()))
                        .build();
    }

    protected void tryRemoveLastCaptcha()
    {
        log.info("try remove last captcha");
        String lastUuid = SaManager.getSaTokenDao().get(CommonConstants.CAPTCHA_SIGN);
        if (lastUuid != null)
        {
            redisUtils.delete(RedisKeyEnum.CAPTCHA_CODE, lastUuid);
        }
    }
}
