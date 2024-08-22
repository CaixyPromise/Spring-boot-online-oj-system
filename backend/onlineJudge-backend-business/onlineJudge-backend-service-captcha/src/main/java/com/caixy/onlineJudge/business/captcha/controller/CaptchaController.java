package com.caixy.onlineJudge.business.captcha.controller;


import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;
import com.caixy.onlineJudge.business.captcha.service.CaptchaService;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码接口控制器
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.controller.CaptchaController
 * @since 2024-07-16 03:59
 **/
@Slf4j
@RestController
@RequestMapping("/")
public class CaptchaController
{
    @Resource
    private CaptchaService captchaService;

    @GetMapping("/get")
    public BaseResponse<CaptchaVO> getCaptcha(HttpServletResponse response)
    {
        return ResultUtils.success(captchaService.getAnyCaptcha(response));
    }
}
