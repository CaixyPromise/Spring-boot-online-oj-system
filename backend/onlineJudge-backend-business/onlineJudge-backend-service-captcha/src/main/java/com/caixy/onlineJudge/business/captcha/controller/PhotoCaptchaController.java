package com.caixy.onlineJudge.business.captcha.controller;


import com.caixy.onlineJudge.common.cache.redis.annotation.DistributedLock;
import com.caixy.onlineJudge.common.cache.redis.annotation.RateLimitFlow;
import com.caixy.onlineJudge.models.enums.redis.RDLockKeyEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisLimiterEnum;
import com.caixy.onlineJudge.models.vo.captcha.CaptchaVO;
import com.caixy.onlineJudge.business.captcha.service.PhotoCaptchaService;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class PhotoCaptchaController
{
    @Resource
    private PhotoCaptchaService photoCaptchaService;

    @GetMapping("/get")
    @RateLimitFlow(key = RedisLimiterEnum.CAPTCHA, args = "#request.getSession().getId()", errorMessage = "验证码获取过于频繁，请稍后再试")
    public BaseResponse<CaptchaVO> getCaptcha(HttpServletResponse response, HttpServletRequest request)
    {
        return ResultUtils.success(photoCaptchaService.getAnyCaptcha(response));
    }
}
