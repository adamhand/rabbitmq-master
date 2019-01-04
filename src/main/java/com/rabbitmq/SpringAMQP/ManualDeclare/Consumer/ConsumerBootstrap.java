package com.rabbitmq.SpringAMQP.ManualDeclare.Consumer;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.ManualDeclare.Consumer")
public class ConsumerBootstrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String QUEUE_NAME = "adamhand.order.add";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConsumerBootstrap.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        MessageListenerContainer msgListenerContainer =
                context.getBean(MessageListenerContainer.class);

        /**
         * 声明一个direct类型的、持久化、非排他的交换器
         */
        rabbitAdmin.declareExchange(new DirectExchange(EXCHANGE_NAME,
                             true, false,
                                    new HashMap<String, Object>()));

        /**
         * 声明一个持久化、非排他、非自动删除的队列
         */
        rabbitAdmin.declareQueue(new Queue(QUEUE_NAME,
                                            true, false, false,
                                                    new HashMap<String, Object>()));

        /**
         * 将交换器和队列绑定
         */
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(QUEUE_NAME)).
                                                        to(new DirectExchange(EXCHANGE_NAME)).
                                                        with(ROUTING_KEY));

        /**
         * 开始监听
         */
        msgListenerContainer.start();
    }
}
