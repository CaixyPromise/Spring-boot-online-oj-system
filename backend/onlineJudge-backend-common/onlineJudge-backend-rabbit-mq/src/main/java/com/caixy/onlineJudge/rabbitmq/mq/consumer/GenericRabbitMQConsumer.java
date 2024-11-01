package com.caixy.onlineJudge.rabbitmq.mq.consumer;


import com.caixy.onlineJudge.common.objectMapper.ObjectMapperUtil;
import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 通用的消息消费类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.core.GenericRabbitMQConsumer
 * @since 2024-06-20 13:15
 **/
@Slf4j
public abstract class GenericRabbitMQConsumer<T> implements RabbitMQMessageHandler<T>
{
    @Resource
    protected RabbitMQUtils rabbitMQUtils;

    private final Class<T> messageType;

    private static final String RETRY_COUNT_KEY = "x-retry-count";
    private static final String X_DEATH_HEADER = "x-death";

    @SuppressWarnings("unchecked")
    public GenericRabbitMQConsumer()
    {
        Type superClass = getClass().getGenericSuperclass();
        log.info("superClass: {}", superClass);
        if (superClass instanceof ParameterizedType)
        {
            Type[] actualTypeArguments = ((ParameterizedType) superClass).getActualTypeArguments();
            log.info("actualTypeArguments: {}", (Object) actualTypeArguments);
            log.info("actualTypeArguments[0]: {}", (Class<T>) actualTypeArguments[0]);
            if (actualTypeArguments[0] instanceof Class<?>)
            {
                this.messageType = (Class<T>) actualTypeArguments[0];
            }
            else
            {
                throw new IllegalArgumentException("Unable to determine the generic type because it's not a class.");
            }
        }
        else
        {
            throw new IllegalArgumentException("Class is not parameterized with generic type.");
        }
    }

    protected T getMessageBody(Message message)
    {
        return ObjectMapperUtil.readValue(message.getBody(), messageType);
    }


    /**
     * 判断是否为死信消息
     */
    private boolean isDeadLetterMessage(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        log.info("header is: {}", headers);
        return headers.containsKey(X_DEATH_HEADER);
    }

    protected void confirmMessage(Channel channel, Message rawMessage)
    {
        try
        {
            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);
        }
        catch (IOException e)
        {
            log.error("Failed to confirm message: {}", rawMessage.getMessageProperties().getMessageId(), e);
        }
    }

    /**
     * 丢弃消息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:10
     */
    protected void discardMessage(Channel channel, Message rawMessage)
    {
        try
        {
            // 设置 requeue 为 false 将不会把消息放回队列，实际上是丢弃了消息
            channel.basicReject(rawMessage.getMessageProperties().getDeliveryTag(), false);
        }
        catch (IOException e)
        {
            log.error("Failed to discard message: {}", rawMessage.getMessageProperties().getMessageId(), e);
        }
    }

    /**
     * 拒绝消息，并重新入队
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:10
     */
    protected void rejectMessage(Channel channel, Message message, boolean requeue) throws IOException
    {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, requeue);
    }

    /**
     * 拒绝消息，并放入死信队列
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:09
     */
    protected void rejectMessage(Channel channel, Message message) throws IOException
    {
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 拒绝消息，并根据最大重试次数进行重试或丢弃
     *
     * @param channel    RabbitMQ 通道对象
     * @param rawMessage 原始消息对象
     * @param maxRetries 最大重试次数
     */
    protected void rejectAndRetryOrDiscard(Channel channel,
                                           Message rawMessage,
                                           Object message,
                                           RabbitMQQueueEnum rabbitMQQueueEnum,
                                           int maxRetries) throws IOException
    {
        long deliveryTag = rawMessage.getMessageProperties().getDeliveryTag();
        Integer retryCount = getRetryCount(rawMessage);

        if (retryCount < maxRetries)
        {
            // 增加重试次数并重新发送消息到延迟队列
            int newRetryCount = retryCount + 1;
            rabbitMQUtils.sendDelayedMessageWithRetry(rabbitMQQueueEnum, message, newRetryCount);
            // 丢弃原始消息，源消息
            discardMessage(channel, rawMessage);
        }
        else
        {
            log.info("达到最大重试次数，拒绝消息，不重新入队（消息会进入死信队列或被丢弃）");
            // 达到最大重试次数，拒绝消息，不重新入队（消息会进入死信队列或被丢弃）
            rejectMessage(channel, rawMessage);
        }
    }


    protected Integer getRetryCount(Message rawMessage)
    {
        return (Integer) rawMessage.getMessageProperties().getHeaders().getOrDefault(RETRY_COUNT_KEY, 0);
    }
}