package com.rabbitmq.AlternateExchange.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MessageConsumer {
    private static final String EXCHANGE_NAME = "adamhand.order.normal";
    private static final String AE_EXCHANGE_NAME = "adamhand.order.ae";
    private static final String QUEUE_NAME = "adamhand.order.add.normal";
    private static final String AE_QUEUE_NAME = "adamhand.order.add.ae";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Channel channel = ChannelUtils.getChannelInstance("message producer");

        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());
        //声明AE 类型为Fanout
        channel.exchangeDeclare(AE_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true, false, false,
                new HashMap<String, Object>());
        // 为adamhand.order设置AE
        Map<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", AE_EXCHANGE_NAME);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, map);
        channel.queueBind(declareOk.getQueue(), EXCHANGE_NAME, ROUTING_KEY, new HashMap<String, Object>());

        // 将AE队列绑定到AE交换机上 无需指定routing key
        AMQP.Queue.DeclareOk declareOk1 = channel.queueDeclare(AE_QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());
        channel.queueBind(declareOk1.getQueue(), AE_EXCHANGE_NAME, "", new HashMap<String, Object>());

        /**
         * 消费正常队列
         */
        channel.basicConsume(declareOk.getQueue(), false, "nomal consumer", new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("normal queue........");
                    System.out.println(new String(body, "UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });

        /**
         * 消费AE队列
         */
        channel.basicConsume(declareOk1.getQueue(), false, "alternate consumer", new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("alternate queue........");
                    System.out.println(new String(body, "UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });
    }
}
