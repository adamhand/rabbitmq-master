package com.rabbitmq.AlternateExchange.SpringAMQP;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TransferQueue;

@ComponentScan(basePackages = "com.rabbitmq.AlternateExchange.SpringAMQP")
public class ProducerBootstrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String EXCHANGE_NAME_AE = "adamhand.order.failure";
    private static final String ROUTING_KEUY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProducerBootstrap.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        /**
         * 声明AE交换机
         */
        rabbitAdmin.declareExchange(
                new FanoutExchange(EXCHANGE_NAME_AE, true, false,
                new HashMap<String, Object>()));

        /**
         * 声明交换机
         */
        Map<String, Object> properties = new HashMap<>();
        properties.put("alternate-exchange", EXCHANGE_NAME_AE);
        rabbitAdmin.declareExchange(
                new DirectExchange(EXCHANGE_NAME, true, false,
                properties));

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
        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEUY+"xxx", msg);
    }
}
