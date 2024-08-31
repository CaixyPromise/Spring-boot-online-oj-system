package com.caixy.onlineJudge.common.email.service.impl;


import com.caixy.onlineJudge.common.email.aspect.EmailCategoryHandler;
import com.caixy.onlineJudge.common.email.config.EmailConfig;
import com.caixy.onlineJudge.common.email.constant.EmailConstant;
import com.caixy.onlineJudge.common.email.models.SendCaptchaEmailDTO;
import com.caixy.onlineJudge.common.email.models.SendOrderInfoDTO;
import com.caixy.onlineJudge.common.email.service.EmailService;
import com.caixy.onlineJudge.common.email.utils.EmailTemplateUtil;
import com.caixy.onlineJudge.common.json.JsonUtils;
import com.caixy.onlineJudge.models.dto.email.EmailSenderRabbitMQConsumerDTO;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮箱服务类实现
 *
 * @name: com.caixy.adminSystem.service.impl.EmailServiceImpl
 * @author: CAIXYPROMISE
 * @since: 2024-01-10 22:01
 **/
@Service
@Slf4j
public class EmailServiceImpl implements EmailService
{
    @Resource
    private JavaMailSender mailSender;

    @Resource
    private EmailConfig emailConfig;

    private static final String SUBJECT_TITLE = "【" + EmailConstant.EMAIL_TITLE + "】-%s-%s";

    private final ConcurrentHashMap<EmailSenderCategoryEnum, Method> emailCategoryHandlerMap = new ConcurrentHashMap<>();

    @Override
    public boolean sendEmail(EmailSenderRabbitMQConsumerDTO<?> emailSenderDTO, EmailSenderCategoryEnum emailSenderCategoryEnum)
    {

        Method methodHandler = emailCategoryHandlerMap.get(emailSenderCategoryEnum);
        if (methodHandler == null)
        {
            log.error("邮件发送类型错误，type:{}", emailSenderDTO.getType());
            return false;
        }
        try
        {
            // 调用对应的处理器
            return (boolean) methodHandler.invoke(this, emailSenderDTO);
        }
        catch (Exception e)
        {
            log.error("邮件发送失败，type:{}", emailSenderDTO.getType(), e);
            return false;
        }
    }

    @PostConstruct
    public void init()
    {
        fillEmailHandlerMap();
    }

    private void fillEmailHandlerMap()
    {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(EmailCategoryHandler.class))
            {
                EmailCategoryHandler handler = method.getAnnotation(EmailCategoryHandler.class);
                emailCategoryHandlerMap.put(handler.value(), method);
            }
        }
        log.info("fillEmailHandlerMap: {}", emailCategoryHandlerMap);
    }

    /**
     * 发送邮箱验证码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/1/10 2:04
     */
    @EmailCategoryHandler(value = EmailSenderCategoryEnum.CAPTCHA)
    protected boolean sendCaptchaEmail(EmailSenderRabbitMQConsumerDTO<?> emailSenderDTO) throws JsonProcessingException
    {
        log.info("sendCaptchaEmail: {}", emailSenderDTO);
        SendCaptchaEmailDTO sendCaptchaEmailDTO = JsonUtils.mapToObject(emailSenderDTO.getData(), SendCaptchaEmailDTO.class);
        return doSendEmail(emailSenderDTO.getToEmail(),
                "验证码",
                EmailTemplateUtil.buildCaptchaEmailContent(EmailConstant.EMAIL_HTML_CONTENT_PATH, sendCaptchaEmailDTO.getCode()),
                EmailSenderCategoryEnum.CAPTCHA);
    }

    /**
     * 发送支付成功信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/1/10 2:04
     */
    @EmailCategoryHandler(value = EmailSenderCategoryEnum.PAYMENT_INFO)
    protected boolean sendPaymentSuccessEmail(EmailSenderRabbitMQConsumerDTO<?> emailSenderDTO)
    {
        SendOrderInfoDTO orderInfoDTO = (SendOrderInfoDTO) emailSenderDTO.getData();
        return doSendEmail(emailSenderDTO.getToEmail(),
                "订单通知-感谢您的购买，请查收您的订单",
                EmailTemplateUtil.buildPaySuccessEmailContent(EmailConstant.EMAIL_HTML_PAY_SUCCESS_PATH,
                        orderInfoDTO.getOrderName(),
                        orderInfoDTO.getOrderTotal()),
                EmailSenderCategoryEnum.PAYMENT_INFO);
    }

    private Boolean doSendEmail(String targetEmailAccount,
                                String subject,
                                String content,
                                EmailSenderCategoryEnum categoryEnum
                                )
    {
        try
        {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            // 邮箱发送内容组成
            helper.setSubject(String.format(SUBJECT_TITLE, categoryEnum.getDesc(), subject));
            helper.setText(content, true);
            helper.setTo(targetEmailAccount);
            helper.setFrom(EmailConstant.EMAIL_TITLE + '<' + emailConfig.getUsername() + '>');
            mailSender.send(message);
            return true;
        }
        catch (MessagingException e)
        {
            log.error("邮件发送失败", e);
            return false;
        }
    }
}
