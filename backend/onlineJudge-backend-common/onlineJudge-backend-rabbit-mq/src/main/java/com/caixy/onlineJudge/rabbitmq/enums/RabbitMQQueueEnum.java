package com.caixy.onlineJudge.rabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;



/**
 * @name: com.caixy.adminSystem.model.enums.RabbitMQQueueEnum
 * @description: 普通消息队列信息枚举封装
 * @author: CAIXYPROMISE
 * @date: 2024-06-19 22:30
 **/
@AllArgsConstructor
@Getter
public enum RabbitMQQueueEnum
{
    /**
     * 订单附件
     */
    USER_ACTIVE("userActionExchange",
            "user.active",
            "Delay-userActiveQueue",
            "X-DeadLetter-Delay-userActive-Queue",
            // 延迟时间，单位为毫秒,
            60L * 60L * 24L * 1000L , // 24小时
            true),
    /**
     * 邮箱验证码
     */
    EMAIL("emailExchange",
    "email.captcha",
    "emailCaptchaQueue",
    "X-DeadLetter-Email-Captcha-Queue",
    null,
    false),

    JUDGE_QUESTION("questionExchange",
            "question.judge",
            "judgeQuestionQueue",
            "X-DeadLetter-Judge-Question-Queue",
            null,
            false),

    ;
    /**
     * 交换机名称
     */
    private final String exchange;

    /**
     * 路由键
     */
    private final String routingKey;

    /**
     * 队列名称
     */
    private final String queueName;

    /**
     * 死信队列名称
     */
    private final String deadLetterQueue;

    /**
     * 延迟时间
     */
    private final Long delayTime;

    /**
     * 是否是延迟队列
     */
    private final Boolean isDelay;



    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static RabbitMQQueueEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (RabbitMQQueueEnum anEnum : RabbitMQQueueEnum.values())
        {
            if (anEnum.routingKey.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }
}
