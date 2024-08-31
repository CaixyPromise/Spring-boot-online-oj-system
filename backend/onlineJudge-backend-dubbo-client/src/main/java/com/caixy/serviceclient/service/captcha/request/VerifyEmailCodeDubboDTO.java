package com.caixy.serviceclient.service.captcha.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 校验图像验证码请求体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.captcha.request.VerifyCodeDubboDTO
 * @since 2024/8/23 上午2:04
 */
@Data
public class VerifyEmailCodeDubboDTO implements Serializable
{
    /**
     * 验证码
     */
    private String code;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 场景
     */
    private Integer scene;

    private static final long serialVersionUID = 1L;

    public static VerifyEmailCodeDubboDTO of(String code, String email, Integer scene)
    {
        VerifyEmailCodeDubboDTO dto = new VerifyEmailCodeDubboDTO();
        dto.setCode(code);
        dto.setEmail(email);
        dto.setScene(scene);
        return dto;
    }
}
