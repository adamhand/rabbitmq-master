package com.rabbitmq.Exception;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;

public class MessageProducer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtils.getChannelInstance("Producer of Order System");
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, false,
                new HashMap<String, Object>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)       //持久化
                .contentType(CONTENT_TYPE)
                .build();

        String msg = "order information";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, basicProperties, msg.getBytes());
    }
}
