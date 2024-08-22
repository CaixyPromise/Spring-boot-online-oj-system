package com.caixy.serviceclient.service.captcha.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码操作通用返回体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.captcha.response.CapchaOperatorResponse
 * @since 2024/8/1 下午7:31
 */
@Data
public class CaptchaOperatorResponse<T> implements Serializable
{
    private T data;
}
