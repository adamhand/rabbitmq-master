package com.rabbitmq.TimeToLive.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MessageProducer {
    private static final String EXCHANGE = "adamhand.order.ttl";
    private static final String QUEUENAME = "adamhand.order.add.ttl";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannelInstance("producer");

        /**
         * 设置队列消息过期时间为10s
         */
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 10000);
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true, false, false,
                new HashMap<String, Object>());
        channel.queueDeclare(QUEUENAME, true, false, false, map);
        channel.queueBind(EXCHANGE, QUEUENAME, ROUTING_KEY, new HashMap<String, Object>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType("UTF-8")
                .build();

        channel.basicPublish(EXCHANGE, ROUTING_KEY, true, basicProperties, "order information".getBytes());

        /**
         * 设置消息过期时间为3s
         */
        AMQP.BasicProperties basicProperties1 = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType("UTF-8")
                .expiration("30000")
                .build();

        channel.basicPublish(EXCHANGE, ROUTING_KEY, true, basicProperties1, "order information ttl".getBytes());
    }
}
