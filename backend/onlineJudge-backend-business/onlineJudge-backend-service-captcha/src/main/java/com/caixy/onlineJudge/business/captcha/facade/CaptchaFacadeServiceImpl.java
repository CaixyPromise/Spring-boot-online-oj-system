package com.caixy.onlineJudge.business.captcha.facade;

import com.caixy.onlineJudge.business.captcha.service.EmailCaptchaService;
import com.caixy.onlineJudge.common.rpc.facade.Facade;
import com.caixy.serviceclient.service.captcha.CaptchaFacadeService;
import com.caixy.serviceclient.service.captcha.request.VerifyEmailCodeDubboDTO;
import com.caixy.serviceclient.service.captcha.response.CaptchaOperatorResponse;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 验证码远程调用接口实现
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.facade.CaptchaFacadeServiceImpl
 * @since 2024/8/29 下午2:50
 */
@DubboService(version = "1.0.0")
public class CaptchaFacadeServiceImpl implements CaptchaFacadeService
{
    @Resource
    private EmailCaptchaService emailCaptchaService;

    @Facade
    @Override
    public CaptchaOperatorResponse<Boolean> verifyEmailCaptcha(VerifyEmailCodeDubboDTO codeDubboDTO)
    {
        return new CaptchaOperatorResponse<>(emailCaptchaService.verifyEmailCaptcha(
                codeDubboDTO.getEmail(),
                codeDubboDTO.getCode(),
                codeDubboDTO.getScene()
        ));
    }
}
