package com.caixy.onlineJudge.business.judge.mq;

import com.caixy.onlineJudge.business.judge.service.JudgeService;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;
import com.caixy.onlineJudge.rabbitmq.mq.consumer.GenericRabbitMQConsumer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 判题服务队列消费者
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.mq.JudgeQuestionConsumer
 * @since 2024/9/11 下午5:59
 */
@Component
@Slf4j
public class JudgeQuestionConsumer extends GenericRabbitMQConsumer<Long>
{
    @Resource
    private JudgeService judgeService;

    @Override
    @RabbitListener(queues = "judgeQuestionQueue", ackMode = "MANUAL")
    public void handleMessage(Channel channel, Message message, String messageId) throws Exception
    {
        Long submitId = getMessageBody(message);
        QuestionSubmit questionSubmit = doJudge(submitId);
        boolean result = questionSubmit != null;
        if (result)
        {
            log.info("submitId: {}, questionId: {} 普通队列判题成功", submitId, questionSubmit.getQuestionId());
            confirmMessage(channel, message);
        }
        else
        {
            discardMessage(channel, message);
        }
    }

    @Override
    public void handleDeadLetterMessage(Channel channel, Message message) throws Exception
    {
        Long submitId = getMessageBody(message);
        QuestionSubmit questionSubmit = doJudge(submitId);
        boolean result = questionSubmit != null;
        if (result)
        {
            log.info("submitId: {}, questionId: {} 死信队列判题成功", submitId, questionSubmit.getQuestionId());
            confirmMessage(channel, message);
        }
        else
        {
            discardMessage(channel, message);
        }
    }

    private QuestionSubmit doJudge(Long submitId)
    {
        if (submitId == null)
        {
            log.error("submitId is null");
        }
        QuestionSubmit questionSubmit = judgeService.handleSubmit(submitId);
        if (questionSubmit == null)
        {
            log.error("submitId: {} 判题失败", submitId);
            return null;
        }
        return questionSubmit;
    }
}
