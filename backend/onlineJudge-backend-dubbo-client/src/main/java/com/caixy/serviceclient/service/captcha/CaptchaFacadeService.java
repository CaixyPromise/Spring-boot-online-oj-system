package com.caixy.serviceclient.service.captcha;

import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;
import com.caixy.serviceclient.service.captcha.response.CaptchaOperatorResponse;

/**
 * 验证码远程调用接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.captcha.CaptchaFacadeService
 * @since 2024/8/1 下午7:26
 */
public interface CaptchaFacadeService
{
    CaptchaOperatorResponse<CaptchaVO> verifyCaptcha(String token, String code);

    CaptchaOperatorResponse<Boolean> generateCaptcha();
}
