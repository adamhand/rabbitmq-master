package com.rabbitmq.DeadLetter.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class MessageProducer {
    private static final String EXCHANGE = "adamhand.order.alive";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannelInstance("producer");

        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true, false, false,
                new HashMap<String, Object>());
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType("UTF-8")
                .build();

        channel.basicPublish(EXCHANGE, ROUTING_KEY, true, basicProperties, "order information".getBytes());
    }
}
