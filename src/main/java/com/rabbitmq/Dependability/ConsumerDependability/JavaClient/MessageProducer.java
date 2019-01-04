package com.rabbitmq.Dependability.ConsumerDependability.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;

public class MessageProducer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtils.getChannelInstance("message producer");

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, false,
                new HashMap<String, Object>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType(CONTENT_TYPE)
                .build();

        String msg1 = "order information 1";
        String msg2 = "order information 2";

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, basicProperties, msg1.getBytes());
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, basicProperties, msg2.getBytes());
    }
}
