package com.rabbitmq.Dependability.ProducerDependability.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

public class MessageProducer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtils.getChannelInstance("Consumer of Order System");

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false,
                new HashMap<String, Object>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                                                        .builder()
                                                        .deliveryMode(2)
                                                        .contentType("UTF-8")
                                                        .build();

        /**
         * 当消息没有被正确路由时 回调ReturnListener
         */
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println("replyCode: "+i);
                System.out.println("replyText: "+s);
                System.out.println("exchange: "+s1);
                System.out.println("routingkey: "+s2);
                System.out.println("properties: "+basicProperties);
                System.out.println("body: "+new String(bytes, "UTF-8"));

            }
        });

        /**
         * 开启消息确认
         */
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("ack............");
                System.out.println(l);
                System.out.println(b);
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("nack.............");
                System.out.println(l);
                System.out.println(b);
            }
        });

        /**
         * 将mandatory属性设置成true
         */
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
                            true,
                               basicProperties,
                               "order information".getBytes());

        //这条消息的ROUTING_KEY是错误的
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY+"xxx",
                true,
                basicProperties,
                "order information".getBytes());
    }
}
