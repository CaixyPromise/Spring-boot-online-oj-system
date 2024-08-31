package com.caixy.onlineJudge.common.rabbitmq.mq.producer;


import com.caixy.onlineJudge.common.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.common.rabbitmq.utils.RabbitMQUtils;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;

/**
 * 通用生产者方法类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.producer.core.GenericRabbitMQProducer
 * @since 2024-06-20 15:15
 **/

public abstract class GenericRabbitMQProducer<T> implements RabbitMQProducerHandler<T>
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GenericRabbitMQProducer.class);
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Resource
    protected RabbitMQUtils rabbitMQUtils;

    protected final RabbitMQQueueEnum queueEnum;

    protected GenericRabbitMQProducer()
    {
        Annotation[] annotations = this.getClass().getAnnotations();
        log.info("getAnnotation: {}", (Object) annotations);
        if (this.getClass().isAnnotationPresent(RabbitProducer.class))
        {
            RabbitProducer rabbitProducer = this.getClass().getAnnotation(RabbitProducer.class);
            queueEnum = rabbitProducer.value();
        }
        else
        {
            throw new RuntimeException("Rabbit生产者初始化失败：RabbitProducer注解未找到");
        }
    }

    // 默认的发送消息方法可以在这里实现，子类可以覆盖这个方法
    @Override
    public void sendMessage(T message)
    {
        if (!queueEnum.getIsDelay())
        {
            rabbitMQUtils.sendMessage(queueEnum, message);
            logger.info("发送消息成功，队列：" + queueEnum.getQueueName() + "，消息：" + message);
        }
        else
        {
            sendDelayMessage(message);
            logger.warning(queueEnum.getQueueName() + ": " + "使用发送普通消息方法" + queueEnum.getQueueName() +
                           "，消息：" + message);
        }
    }


    @Override
    public void sendDelayMessage(T message)
    {
        rabbitMQUtils.sendDelayedMessage(queueEnum, message);

        logger.info("发送延时消息成功，队列：" + queueEnum.getQueueName() + "，消息：" + message);
    }
}
