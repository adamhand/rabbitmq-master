package com.rabbitmq.SpringAMQP.Annotation.Producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.SpringAMQP.MessageConverter.entity.Order;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.Annotation.Producer")
public class ProducerBootstrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProducerBootstrap.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        /**
         * 声明交换机
         */
        rabbitAdmin.declareExchange(
                new DirectExchange(EXCHANGE_NAME, true, false,
                        new HashMap<String, Object>()));

        /**
         * 声明要发送的消息
         */
        MessageProperties msgProperties = new MessageProperties();
        msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties.setContentType(CONTENT_TYPE);
        Message msg = new Message("order information".getBytes(), msgProperties);

        /**
         * 发布消息
         */
        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, msg);
    }
}
