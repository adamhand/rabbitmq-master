package com.rabbitmq.SpringAMQP.MsgListenerAdapte.Consumer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.MsgListenerAdapte.Consumer")
public class ConsumerBootstrap {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
    }
}
