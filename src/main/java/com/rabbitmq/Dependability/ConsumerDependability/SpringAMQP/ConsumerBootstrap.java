package com.rabbitmq.Dependability.ConsumerDependability.SpringAMQP;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.rabbitmq.Dependability.ConsumerDependability.SpringAMQP")
public class ConsumerBootstrap {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
    }
}
