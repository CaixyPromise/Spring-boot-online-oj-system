package com.caixy.onlineJudge.models.vo.captcha;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证码VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.captcha.CaptchaVO
 * @since 2024-07-15 19:44
 **/
@Data
@Builder
public class CaptchaVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    /**
     * 验证码图片-base64
     */
    private String codeImage;
}