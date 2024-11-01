package com.caixy.serviceclient.service.captcha.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证图像验证码请求体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.captcha.request.VerifyPhotoCodeDubboDTO
 * @since 2024/9/5 下午9:45
 */
@Data
@AllArgsConstructor
public class VerifyPhotoCodeDubboDTO implements Serializable
{
    /**
     * 验证码
     */
    private String code;

    public static VerifyPhotoCodeDubboDTO of(String code)
    {
        return new VerifyPhotoCodeDubboDTO(code);
    }
    private static final long serialVersionUID = 1L;
}
