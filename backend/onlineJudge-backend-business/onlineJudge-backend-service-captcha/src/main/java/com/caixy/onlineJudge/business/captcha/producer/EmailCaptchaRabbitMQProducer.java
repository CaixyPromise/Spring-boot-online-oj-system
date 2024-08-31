package com.caixy.onlineJudge.business.captcha.producer;

import com.caixy.onlineJudge.common.email.models.SendCaptchaEmailDTO;
import com.caixy.onlineJudge.common.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.common.rabbitmq.mq.producer.GenericRabbitMQProducer;
import com.caixy.onlineJudge.common.rabbitmq.mq.producer.RabbitProducer;
import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
/**
 * 验证码发送生产者
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.captcha.producer.EmailCaptchaRabbitMQProducer
 * @since 2024/8/27 下午5:02
 */
@Slf4j
@Component
@RabbitProducer(RabbitMQQueueEnum.EMAIL_CODE)
public class EmailCaptchaRabbitMQProducer extends GenericRabbitMQProducer<EmailSenderRabbitMQConsumerDTO<SendCaptchaEmailDTO>>
{
    public void sendEmailCaptcha(String toEmail, String captchaCode)
    {
        SendCaptchaEmailDTO sendCaptchaEmailDTO = new SendCaptchaEmailDTO(captchaCode);
        EmailSenderRabbitMQConsumerDTO.EmailSenderRabbitMQConsumerDTOBuilder<SendCaptchaEmailDTO> consumerDTOBuilder = EmailSenderRabbitMQConsumerDTO.builder();
        sendMessage(consumerDTOBuilder
                .toEmail(toEmail)
                .data(sendCaptchaEmailDTO)
                .type(EmailSenderCategoryEnum.CAPTCHA.getCode())
                .build());
    }
}
