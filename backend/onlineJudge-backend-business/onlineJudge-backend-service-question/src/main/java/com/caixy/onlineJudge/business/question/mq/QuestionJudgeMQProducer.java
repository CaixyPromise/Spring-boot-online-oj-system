package com.caixy.onlineJudge.business.question.mq;

import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.rabbitmq.mq.producer.GenericRabbitMQProducer;
import com.caixy.onlineJudge.rabbitmq.mq.producer.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 提交题目判题方法
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.question.mq.QuestionJudgeMQProducer
 * @since 2024/9/11 上午1:42
 */
@Slf4j
@Component
@RabbitProducer(RabbitMQQueueEnum.JUDGE_QUESTION)
public class QuestionJudgeMQProducer extends GenericRabbitMQProducer<Long>
{
    /**
     * 提交题目，发送id进入队列
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 上午1:46
     */
    public void sendJudgeQuestionMessage(Long submitId)
    {
        sendMessage(submitId);
    }
}
