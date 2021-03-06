package com.rabbitmq.Dependability.ProducerDependability.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

public class MessageConsumer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtils.getChannelInstance("Consumer of order system");

        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare("roberto.order.add",
                                                                true, false, false,
                                                                   new HashMap<String, Object>());

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                                true, false, false,
                                    new HashMap<String, Object>());

        channel.queueBind(declareOk.getQueue(), EXCHANGE_NAME, ROUTING_KEY, new HashMap<String, Object>());

        channel.basicConsume(declareOk.getQueue(), true, "Producer of Order System", new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(consumerTag);
                System.out.println(envelope.toString());
                System.out.println(properties.toString());
                System.out.println("message information"+ new String(body));
            }
        });
    }
}
