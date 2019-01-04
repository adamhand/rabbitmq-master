package com.rabbitmq.Exception;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.impl.DefaultExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class ChannelUtils {
    private static final String IPADDRESS = "192.168.1.107";
    private static final int PORT = 5672;
    private static final String VIRTUALHOST = "/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    /**
     * 获取Channel
     * @param connDescription
     * @return
     */
    public static Channel getChannelInstance(String connDescription){
        try {
            ConnectionFactory factory = getConnectionFactory();
            Connection connection = factory.newConnection(connDescription);
            return connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException("failed to create channel");
        }
    }

    /**
     * 生成ConnectionFactory
     * @return
     */
    private static ConnectionFactory getConnectionFactory(){
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

        /**
         * 自定义异常处理器
         */
        factory.setExceptionHandler(new DefaultExceptionHandler(){
            @Override
            public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
                System.out.println("consumer exception happens");
                System.out.println("exception logs: "+exception.getMessage());
                super.handleConsumerException(channel, exception, consumer, consumerTag, methodName);
            }
        });

        return factory;
    }
}
