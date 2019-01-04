package com.rabbitmq.Dependability.ProducerDependability.SpringAMQP;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.rabbitmq.Dependability.ProducerDependability.SpringAMQP")
public class ConsumerBootstrap {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
    }
}
