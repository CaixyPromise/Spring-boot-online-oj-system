package com.caixy.onlineJudge.models.enums.email;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * email发送类型枚举
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnums
 * @since 2024/8/24 上午1:06
 */
@AllArgsConstructor
@Getter
public enum EmailSenderCategoryEnum
{
    REGISTER(1, "注册邮件"),
    FORGET_PASSWORD(2, "忘记密码邮件"),
    CAPTCHA(3, "验证码邮件"),
    PAYMENT_INFO(4, "支付信息")
    ;

    private final Integer code;
    private final String desc;

    public static EmailSenderCategoryEnum getEnumByCode(Integer code)
    {
        if (code == null || code < 0)
        {
            return null;
        }
        for (EmailSenderCategoryEnum emailSenderCategoryEnum : values())
        {
            if (emailSenderCategoryEnum.getCode().equals(code))
            {
                return emailSenderCategoryEnum;
            }
        }
        return null;
    }
}
