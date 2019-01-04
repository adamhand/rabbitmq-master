package com.rabbitmq.HelloWorldDemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY =   "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IPADDRESS = "192.168.1.107";
    private static final int PORT = 5672;        //RabbitMQ服务端默认端口号为5672

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IPADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");     //要连接到RabbitMQ的用户名
        factory.setPassword("root");     //要连接到RabbitMQ的密码

        Connection connection = factory.newConnection();   //创建连接
        Channel channel = connection.createChannel();      //创建channel
        //创建一个交换类型为direct、持久化、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        //创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //将交换机与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        //发送一条持久化消息：hello world
        String msg = "hello world";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                msg.getBytes());

        //关闭资源
        channel.close();
        connection.close();
    }
}
