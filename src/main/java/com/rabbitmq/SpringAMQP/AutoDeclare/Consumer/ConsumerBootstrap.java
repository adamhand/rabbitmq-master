package com.rabbitmq.SpringAMQP.AutoDeclare.Consumer;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.AutoDeclare.Consumer")
public class ConsumerBootstrap {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
    }
}
