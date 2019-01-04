package com.rabbitmq.RabbitmqRpc.SpringAMQP;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RpcServerConfig {
    private static final String IPADDRESS = "192.168.1.107";
    private static final int PORT = 5672;
    private static final String VIRTUALHOST = "/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String QUEUE_NAMES = "adamhand.order.add";
    private static final String EXCHANGE_NAMES = "adamhand.order";
    private static final String ROUTING_KEY = "add";
    private static final int CONSUMER_NUMBER = 5;
    private static final int MAX_CONSUMER_NUMBER = 10;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();

        /**
         * 配置连接信息
         */
        factory.setHost(IPADDRESS);
        factory.setPort(PORT);
        factory.setVirtualHost(VIRTUALHOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        /**
         * 网络异常自动回复，每隔10秒重连一次
         */
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(1000);

        /**
         * factory的属性
         */
        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("prindipal", "adamhand");
        propertiesMap.put("description", "Order System");
        propertiesMap.put("email", "adaihand@163.com");
        factory.setClientProperties(propertiesMap);

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(factory);

        return cachingConnectionFactory;
    }

    /**
     * 用于声明交换机 队列 绑定等
     * @param factory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory factory){
        return new RabbitAdmin(factory);
    }

    /**
     * 自动声明队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAMES, true , false, false,
                new HashMap<String, Object>());
    }

    @Bean
    public Exchange exchange(){
        return new DirectExchange(EXCHANGE_NAMES, true, false,
                new HashMap<String, Object>());
    }

    @Bean
    public Binding binding(){
        return new Binding(QUEUE_NAMES, Binding.DestinationType.QUEUE,
                EXCHANGE_NAMES, ROUTING_KEY, new HashMap<String, Object>());
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

        RpcMethod rpcMethod = new RpcMethod();
        MessageListenerAdapter adapter = new MessageListenerAdapter(rpcMethod);
        adapter.setDefaultListenerMethod("addOrder");

        msgLIstenerContainer.setMessageListener(adapter);

        return msgLIstenerContainer;
    }
}
