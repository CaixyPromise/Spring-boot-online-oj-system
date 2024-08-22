package com.caixy.onlineJudge.business.captcha.service;


import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证码服务类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.service.CaptchaService
 * @since 2024-07-16 04:08
 **/
public interface CaptchaService
{
    /**
     * 随机获取一个验证码服务类
     */
    CaptchaVO getAnyCaptcha(HttpServletResponse response);

    /**
     * 获取指定类型的验证码服务类
     */
    CaptchaVO getCaptchaByType(String type, HttpServletResponse response);

    boolean verifyCaptcha(String code);
}
