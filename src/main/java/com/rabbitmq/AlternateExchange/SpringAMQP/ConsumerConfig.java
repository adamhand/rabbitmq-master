package com.rabbitmq.AlternateExchange.SpringAMQP;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConsumerConfig {
    private static final String IPADDRESS = "192.168.1.107";
    private static final int PORT = 5672;
    private static final String VIRTUALHOST = "/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String QUEUE_NAMES = "adamhand.order.add";
    private static final String QUEUE_NAMES_AE = "adamhand.order.add.failure";
    private static final String EXCHANGE_NAMES = "adamhand.order";
    private static final String EXCHANGE_NAMES_AE = "adamhand.order.failure";
    private static final String ROUTING_KEY = "add";
    private static final int CONSUMER_NUMBER = 5;
    private static final int MAX_CONSUMER_NUMBER = 10;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(IPADDRESS);
        factory.setPort(PORT);
        factory.setVirtualHost(VIRTUALHOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(1000);

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("prindipal", "adamhand");
        propertiesMap.put("description", "Order System");
        propertiesMap.put("email", "adaihand@163.com");
        factory.setClientProperties(propertiesMap);

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(factory);

        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory factory){
        return new RabbitAdmin(factory);
    }

    /**
     * 自动声明队列
     * @return
     */
    @Bean
    public List<Queue> queueList(){
        Queue queue1 = new Queue(QUEUE_NAMES, true , false, false,
                new HashMap<String, Object>());

        Queue queue2 = new Queue(QUEUE_NAMES_AE, true, false, false,
                new HashMap<String, Object>());

        return Arrays.asList(queue1, queue2);
    }

    @Bean
    public List<Exchange> exchangeList(){
        Exchange fanoutExchange = new FanoutExchange(EXCHANGE_NAMES, true, false,
                new HashMap<String, Object>());

        Map<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", EXCHANGE_NAMES_AE);
        Exchange directExchange = new DirectExchange(EXCHANGE_NAMES, true, false,
                map);

        return Arrays.asList(fanoutExchange, directExchange);
    }

    @Bean
    public List<Binding> bindingList(){
        Binding binding1 = new Binding(QUEUE_NAMES, Binding.DestinationType.QUEUE,
                EXCHANGE_NAMES, ROUTING_KEY, new HashMap<String, Object>());

        Binding binding2 = new Binding(QUEUE_NAMES_AE, Binding.DestinationType.QUEUE,
                EXCHANGE_NAMES_AE, "", new HashMap<String, Object>());

        return Arrays.asList(binding1, binding2);
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
        msgLIstenerContainer.setQueueNames(QUEUE_NAMES);

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
                    System.out.println(EXCHANGE_NAMES+"............");
                    System.out.println(new String(message.getBody(), "UTF-8"));
                    System.out.println(message.getMessageProperties());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        return msgLIstenerContainer;
    }


    /**
     * AE交换器的处理
     * @param factory
     * @return
     */
    @Bean
    public MessageListenerContainer messageListenerContainer1(CachingConnectionFactory factory){
        SimpleMessageListenerContainer msgLIstenerContainer = new SimpleMessageListenerContainer();
        msgLIstenerContainer.setConnectionFactory(factory);
        msgLIstenerContainer.setQueueNames(QUEUE_NAMES_AE);

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
                return "Consumers of Order System AE";
            }
        });

        /**
         * 消息后置处理器。接收到消息之后打印出来。
         */
        msgLIstenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(EXCHANGE_NAMES_AE+"............");
                    System.out.println(new String(message.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        return msgLIstenerContainer;
    }
}
