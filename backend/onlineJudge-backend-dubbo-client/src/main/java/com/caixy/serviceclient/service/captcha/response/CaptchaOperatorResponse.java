package com.caixy.serviceclient.service.captcha.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 验证码操作通用返回体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.captcha.response.CapchaOperatorResponse
 * @since 2024/8/1 下午7:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CaptchaOperatorResponse extends BaseResponse<Boolean>
{
    private Boolean data;
}
