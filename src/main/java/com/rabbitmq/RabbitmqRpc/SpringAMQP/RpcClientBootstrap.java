package com.rabbitmq.RabbitmqRpc.SpringAMQP;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.rabbitmq.RabbitmqRpc.SpringAMQP")
public class RpcClientBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RpcClientBootstrap.class);

        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");

        Message message = new Message("order information".getBytes(), messageProperties);

        /**
         * 如果超时未返回 则messageReturn为null 可以通过rabbitTemplate.setReplyTimeout(50000);设置超时时间
         */
        rabbitTemplate.setReplyTimeout(50000);
        Object messageReturn = rabbitTemplate.sendAndReceive("adamhand.order",
                            "add",
                            message,
                            new CorrelationData("201911215223"));
        System.out.println(messageReturn);
    }
}
