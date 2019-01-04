package com.rabbitmq.SpringBootAMQP;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class ProducerStrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";

    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate){
        ProducerStrap.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerStrap.class);

        MessageProperties properties = new MessageProperties();
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        properties.setContentType("UTF-8");

        Message msg = new Message("order information".getBytes(), properties);
        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, msg);
    }
}
