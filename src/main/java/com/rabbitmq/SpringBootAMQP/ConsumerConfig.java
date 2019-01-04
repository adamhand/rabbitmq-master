package com.rabbitmq.SpringBootAMQP;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfig {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String QUEUE_NAME = "adamhand.order.add";
    private static final String ROUTING_KEY = "add";
    private static final int CONSUMER_NUMBER = 5;
    private static final int MAX_CONSUMER_NUMBER = 10;

    @Bean
    public Exchange exchange(){
        return new DirectExchange(EXCHANGE_NAME, true, false,
                new HashMap<String, Object>());
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());
    }

    @Bean
    public Binding binding(){
        return new Binding(QUEUE_NAME, Binding.DestinationType.QUEUE, EXCHANGE_NAME, ROUTING_KEY,
                new HashMap<String, Object>());
    }

    /**
     * 监听容器 为消息入队提供异步处理
     * @param factory
     * @return
     */
    @Bean
    public MessageListenerContainer messageListenerContainer(CachingConnectionFactory factory){
        SimpleMessageListenerContainer msgLIstenerContainer = new SimpleMessageListenerContainer();
        msgLIstenerContainer.setConnectionFactory(factory);
        msgLIstenerContainer.setQueueNames(QUEUE_NAME);

        /**
         * 设置消费者线程数和最大线程数
         */
        msgLIstenerContainer.setConcurrentConsumers(CONSUMER_NUMBER);
        msgLIstenerContainer.setMaxConcurrentConsumers(MAX_CONSUMER_NUMBER);

        /**
         * 设置消费者属性
         */
        Map<String, Object> argsMap = new HashMap<>();
        msgLIstenerContainer.setConsumerArguments(argsMap);

        /**
         * 设置消费者标签
         */
        msgLIstenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return "Consumers of Order System";
            }
        });

        /**
         * 消息后置处理器。接收到消息之后打印出来。
         */
        msgLIstenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(new String(message.getBody(), "UTF-8"));
                    System.out.println(message.getMessageProperties());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        return msgLIstenerContainer;
    }
}
