package com.caixy.onlineJudge.common.email.mq;

import com.caixy.onlineJudge.common.email.constant.EmailConstant;
import com.caixy.onlineJudge.common.email.service.EmailService;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.rabbitmq.mq.consumer.GenericRabbitMQConsumer;
import com.caixy.onlineJudge.common.regex.RegexUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 邮件发送消息队列消费者
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.email.consumer.EmailSenderRabbitMQConsumer
 * @since 2024/8/24 上午12:57
 */
@Slf4j
@Service
public class EmailSenderRabbitMQConsumer extends GenericRabbitMQConsumer<EmailSenderRabbitMQConsumerDTO>
{
    @Resource
    private EmailService emailService;
    @RabbitListener(queues = EmailConstant.EMAIL_QUEUE, ackMode = "MANUAL")
    @Override
    public void handleMessage(Channel channel, Message message,
                              String messageId) throws IOException
    {
        // 手动反序列化消息
        EmailSenderRabbitMQConsumerDTO messageDto = getMessageBody(message);
        if (messageDto != null)
        {
            log.info("receive message: {}", messageDto);
            doSender(messageDto, channel, message);
        }
    }

    @RabbitListener(queues = EmailConstant.EMAIL_DEAD_QUEUE, ackMode = "MANUAL")
    @Override
    public void handleDeadLetterMessage(Channel channel, Message message)
    {
        EmailSenderRabbitMQConsumerDTO messageDto = getMessageBody(message);
        if (messageDto != null)
        {
            doSender(messageDto, channel, message);
        }
    }

    private void doSender(EmailSenderRabbitMQConsumerDTO message, Channel channel, Message rawMessage)
    {
        if (message == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮件发送消息队列消费者，消息为空");
        }
        Integer messageType = message.getType();
        EmailSenderCategoryEnum enumByCode = EmailSenderCategoryEnum.getEnumByCode(messageType);
        ThrowUtils.throwIf(enumByCode == null, ErrorCode.PARAMS_ERROR, "邮件发送消息队列消费者，消息类型错误");

        String toEmail = message.getToEmail();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(toEmail), ErrorCode.PARAMS_ERROR,
                "邮件发送消息队列消费者，收件人邮箱为空");
        ThrowUtils.throwIf(!RegexUtils.isEmail(toEmail), ErrorCode.PARAMS_ERROR,
                "邮件发送消息队列消费者，收件人邮箱格式错误");

        boolean sendEmailResult = emailService.sendEmail(message, enumByCode);
        ThrowUtils.throwIf(
                !sendEmailResult,
                ErrorCode.OPERATION_ERROR,
                "邮件发送失败",
                () -> discardMessage(channel, rawMessage));

        confirmMessage(channel, rawMessage);
    }
}
