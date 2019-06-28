package com.lwj.emailserverproducer.service.impl;

import com.lwj.emailserverproducer.service.EmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auth: lwj
 * @Date: 2019/6/27 9:27
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange}")
    private String exchange;
    @Value("${mq.routekey}")
    private String routeKey;

    @Override
    public void sendEmail(String message) throws Exception {

        try {
            rabbitTemplate.convertAndSend(exchange, routeKey, message);
        } catch (Exception e) {
            logger.error("EmailServiceImpl.sendEmail", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void sendEmail(Object obj) throws Exception {
        try {
            rabbitTemplate.convertAndSend(exchange, routeKey, obj);
        } catch (Exception e) {
            logger.error("EmailServiceImpl.sendEmail", ExceptionUtils.getMessage(e));
        }
    }
}
