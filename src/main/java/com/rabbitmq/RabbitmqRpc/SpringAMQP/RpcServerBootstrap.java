package com.rabbitmq.RabbitmqRpc.SpringAMQP;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

@ComponentScan(basePackages = "com.rabbitmq.RabbitmqRpc.SpringAMQP")
public class RpcServerBootstrap {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(RpcServerBootstrap.class);
    }
}
