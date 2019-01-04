package com.rabbitmq.SpringAMQP.Annotation.Consumer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableRabbit
@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.Annotation.Consumer")
public class ConsumerBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
    }
}
