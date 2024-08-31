package com.caixy.onlineJudge.common.email.service;

import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;

/**
 * @Name: com.caixy.adminSystem.service.EmailService
 * @Description: 邮箱服务类
 * @Author: CAIXYPROMISE
 * @Date: 2024-01-10 22:00
 **/
public interface EmailService
{

    boolean sendEmail(EmailSenderRabbitMQConsumerDTO<?> emailSenderDTO, EmailSenderCategoryEnum emailSenderCategoryEnum);
}
