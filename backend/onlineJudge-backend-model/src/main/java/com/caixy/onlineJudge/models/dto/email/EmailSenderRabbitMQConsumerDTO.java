package com.caixy.onlineJudge.models.dto.email;

import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 邮件发送消息队列DTO
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO
 * @since 2024/8/24 上午1:03
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EmailSenderRabbitMQConsumerDTO<T> implements Serializable
{
    /**
     * 邮件类型 {@link EmailSenderCategoryEnum}
     */
    private Integer type;

    /**
     * 邮件内容数据
     */
    private T data;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    private static final long serialVersionUID = 1L;
}
