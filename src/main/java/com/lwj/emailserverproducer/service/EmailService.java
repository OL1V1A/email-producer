package com.lwj.emailserverproducer.service;

/**
 * @Auth: lwj
 * @Date: 2019/6/27 9:25
 */
public interface EmailService {

    /**
     * 发送邮件到消息队列
     *
     * @param message
     * @throws Exception
     */
    void sendEmail(String message) throws Exception;

    /**
     * 发送邮件到消息队列
     *
     * @param obj
     * @throws Exception
     */
    void sendEmail(Object obj) throws Exception;
}
