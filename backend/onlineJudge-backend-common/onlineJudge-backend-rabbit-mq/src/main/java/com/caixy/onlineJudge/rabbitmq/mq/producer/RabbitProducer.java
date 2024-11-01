package com.caixy.onlineJudge.rabbitmq.mq.producer;

import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @name: com.caixy.adminSystem.mq.producer.core.RabbitProducer
 * @description: RabbitMQ生产者注解
 * @author: CAIXYPROMISE
 * @date: 2024-06-30 15:59
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RabbitProducer
{
    RabbitMQQueueEnum value();
}
