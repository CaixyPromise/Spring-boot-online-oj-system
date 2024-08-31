package com.caixy.onlineJudge.common.email.models;

import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 发送订单信息DTO
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.models.SendOrderInfoDTO
 * @since 2024/8/24 下午2:26
 */
@Data
public class SendOrderInfoDTO implements Serializable
{
    private String orderName;
    private String orderTotal;
    private Long orderId;
    private static final long serialVersionUID = 1L;
}
