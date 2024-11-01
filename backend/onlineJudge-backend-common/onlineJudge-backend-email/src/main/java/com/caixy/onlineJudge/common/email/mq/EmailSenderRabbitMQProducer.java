package com.caixy.onlineJudge.common.email.mq;

import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.rabbitmq.mq.producer.GenericRabbitMQProducer;
import com.caixy.onlineJudge.rabbitmq.mq.producer.RabbitProducer;
import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 验证码发送生产者
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.consumer.EmailCaptchaRabbitMQProducer
 * @since 2024/8/27 下午5:02
 */
@Slf4j
@Component
@RabbitProducer(RabbitMQQueueEnum.EMAIL)
public class EmailSenderRabbitMQProducer<T> extends GenericRabbitMQProducer<EmailSenderRabbitMQConsumerDTO<T>>
{
    public void sendEmail(String toEmail, T emailBody, EmailSenderCategoryEnum senderCategoryEnum)
    {
        EmailSenderRabbitMQConsumerDTO.EmailSenderRabbitMQConsumerDTOBuilder<T> consumerDTOBuilder = EmailSenderRabbitMQConsumerDTO.builder();
        sendMessage(consumerDTOBuilder
                .toEmail(toEmail)
                .data(emailBody)
                .type(senderCategoryEnum.getCode())
                .build());
    }
}
