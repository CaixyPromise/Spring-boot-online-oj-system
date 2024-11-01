package com.caixy.onlineJudge.business.captcha.controller;

import com.caixy.onlineJudge.business.captcha.service.EmailCaptchaService;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.captcha.EmailSenderCaptchaRequest;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Email验证码服务
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.controller.EmailCaptchaController
 * @since 2024/8/26 下午2:53
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailCaptchaController
{
    @Resource
    private EmailCaptchaService emailCaptchaService;

    @PostMapping("/send")
    public BaseResponse<Boolean> sendCaptcha(@RequestBody EmailSenderCaptchaRequest emailSenderCaptchaRequest)
    {
        Integer type = emailSenderCaptchaRequest.getType();
        EmailSenderCategoryEnum enumByCode = EmailSenderCategoryEnum.getEnumByCode(type);
        if (enumByCode == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "发送类型错误");
        }
        emailCaptchaService.doSend(emailSenderCaptchaRequest.getToEmail(), enumByCode);
        return ResultUtils.success(true);
    }
}
