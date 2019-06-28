package com.lwj.emailserverproducer.config;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @Auth: lwj
 * @Date: 2019/6/27 8:57
 */
@Configuration
@ComponentScan(basePackages = {"com.lwj.emailserverproducer","com.lwj.common"})
@PropertySource(value = {"classpath:application.properties"})
public class RabbitMQConfig {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(env.getProperty("mq.host").trim());
        factory.setPort(Integer.valueOf(env.getProperty("mq.port").trim()));
        factory.setUsername(env.getProperty("mq.username").trim());
        factory.setPassword(env.getProperty("mq.password"));
        factory.setVirtualHost(env.getProperty("mq.vhost").trim());
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws Exception {
        return new CachingConnectionFactory(connectionFactory());

    }

    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin() throws Exception {
        return new RabbitAdmin(cachingConnectionFactory());
    }

    @Bean
    public Queue queue() {
        String name = env.getProperty("mq.queue").trim();
        //是否持久化
        boolean durable = StringUtils.isNotBlank(env.getProperty("mq.queue.durable").trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.durable").trim()) : true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = StringUtils.isNotBlank(env.getProperty("mq.queue.exclusive").trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.exclusive").trim()) : false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isNotBlank(env.getProperty("mq.queue.autoDelete").trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.autoDelete").trim()) : false;
        return new Queue(name, durable, exclusive, autoDelete);
    }

    @Bean
    public TopicExchange topicExchange() {
        String name = env.getProperty("mq.exchange").trim();
        //是否持久化
        boolean durable = StringUtils.isNotBlank(env.getProperty("mq.exchange.durable").trim()) ?
                Boolean.valueOf(env.getProperty("mq.exchange.durable").trim()) : true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isNotBlank(env.getProperty("mq.exchange.autoDelete").trim()) ?
                Boolean.valueOf(env.getProperty("mq.exchange.autoDelete").trim()) : false;
        return new TopicExchange(name, durable, autoDelete);
    }

    @Bean
    public Binding binding() {
        String routeKey = env.getProperty("mq.routekey").trim();
        return BindingBuilder.bind(queue()).to(topicExchange()).with(routeKey);
    }


}
