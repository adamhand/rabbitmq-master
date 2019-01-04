package com.rabbitmq.Dependability.ConsumerDependability.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

public class MessageConsumer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String QUEUE_NAME = "adamhand.order.add";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) throws IOException {
        final Channel channel = ChannelUtils.getChannelInstance("order system consumer");

        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false,
                new HashMap<String, Object>());
        channel.queueBind(declareOk.getQueue(), EXCHANGE_NAME, ROUTING_KEY, new HashMap<String, Object>());

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(consumerTag);
                    System.out.println(envelope.toString());
                    System.out.println(properties.toString());
                    System.out.println("message information: "+new String(body));
                    if("order information 2".equals(new String(body))){
                        throw new RuntimeException();
                    }else { //消息确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                } catch (Exception e) {  //消息拒绝
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, consumer);
    }
}
