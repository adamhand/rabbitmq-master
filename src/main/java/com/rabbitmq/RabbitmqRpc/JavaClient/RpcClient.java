package com.rabbitmq.RabbitmqRpc.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;
import javafx.scene.input.TouchEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RpcClient {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String QUEUE_NAME = "adamhand.order.add";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtils.getChannelInstance("rpc client");

        channel.queueDeclare(QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false,
                new HashMap<String, Object>());
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, new HashMap<String, Object>());

        String replyTo = "adamhand.order.add.reply";
        channel.queueDeclare(replyTo, true, false, false,
                new HashMap<String, Object>());
        String correlationId = UUID.randomUUID().toString();
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType(CONTENT_TYPE)
                .correlationId(correlationId)
                .replyTo(replyTo)
                .build();
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, basicProperties, "order information".getBytes());

        channel.basicConsume(replyTo, true, "rpc client", new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("rpc result............");
                System.out.println(consumerTag);
                System.out.println("information properties: "+properties);
                System.out.println("information body: "+new String(body));
            }
        });
    }
}
