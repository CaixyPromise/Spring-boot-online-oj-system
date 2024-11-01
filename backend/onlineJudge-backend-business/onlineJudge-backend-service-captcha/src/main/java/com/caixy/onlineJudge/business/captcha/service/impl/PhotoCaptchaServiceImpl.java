package com.caixy.onlineJudge.business.captcha.service.impl;

import com.caixy.onlineJudge.business.captcha.factory.CaptchaFactory;
import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;
import com.caixy.onlineJudge.business.captcha.service.PhotoCaptchaService;
import com.caixy.onlineJudge.business.captcha.strategy.CaptchaGenerationStrategy;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证码服务类接口实现类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.service.impl.CaptchaServiceImpl
 * @since 2024-07-16 04:09
 **/
@Service
@AllArgsConstructor
public class PhotoCaptchaServiceImpl implements PhotoCaptchaService
{
    private final CaptchaFactory captchaFactory;

    @Override
    public CaptchaVO getAnyCaptcha(HttpServletResponse response)
    {
        CaptchaGenerationStrategy randomCaptchaStrategy = captchaFactory.getRandomCaptchaStrategy();
        return randomCaptchaStrategy.generateCaptcha(response);
    }

    @Override
    public CaptchaVO getCaptchaByType(String type, HttpServletResponse response)
    {
        CaptchaGenerationStrategy captchaStrategy = captchaFactory.getCaptchaStrategy(type);
        if (captchaStrategy == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码类型错误");
        }
        return captchaStrategy.generateCaptcha(response);
    }

    @Override
    public boolean verifyCaptcha(String code)
    {
        return captchaFactory.verifyCaptcha(code);
    }
}
