package com.rabbitmq.Dependability.ConsumerDependability.SpringAMQP;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

@ComponentScan(basePackages = "com.rabbitmq.Dependability.ConsumerDependability.SpringAMQP")
public class ProducerBootstrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEUY = "add";
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
        MessageProperties msgProperties1 = new MessageProperties();
        msgProperties1.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties1.setContentType(CONTENT_TYPE);
        Message msg1 = new Message("order information 1".getBytes(), msgProperties1);
        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEUY, msg1, new CorrelationData("201910704116"));

        MessageProperties msgProperties2 = new MessageProperties();
        msgProperties2.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties2.setContentType(CONTENT_TYPE);
        Message msg2 = new Message("order information 2".getBytes(), msgProperties2);
        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEUY, msg2, new CorrelationData("201910704116"));
    }
}
