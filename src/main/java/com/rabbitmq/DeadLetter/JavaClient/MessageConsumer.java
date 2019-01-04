package com.rabbitmq.DeadLetter.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MessageConsumer {
    private static final String DEAD_LETTER_EXCHANGE = "adamhand.order.dead";
    private static final String DEAD_LETTER_EXCHANGE_QUEUE = "adamhand.order.dead.add";
    private static final String EXCHANGE = "adamhand.order.alive";
    private static final String EXCHANGE_QUEUE = "adamhand.order.alive.add";
    private static final String ROUTING_KEY = "add";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Channel channel = ChannelUtils.getChannelInstance("consumer");

        // 声明Dead Letter Exchange
        channel.exchangeDeclare(DEAD_LETTER_EXCHANGE, BuiltinExchangeType.FANOUT, true, false, false,
                new HashMap<String, Object>());

        // 声明队列并 指定x-dead-letter-exchange
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);

        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(EXCHANGE_QUEUE,
                true, false, false, queueProperties);
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true, false, false,
                new HashMap<String, Object>());
        channel.queueBind(declareOk.getQueue(), EXCHANGE, ROUTING_KEY, new HashMap<String, Object>());


        // 将死信队列绑定到死信交换机上 无需指定routing key
        AMQP.Queue.DeclareOk declareOk2 = channel.queueDeclare(DEAD_LETTER_EXCHANGE_QUEUE, true, false, false,
                new HashMap<String, Object>());
        channel.queueBind(declareOk2.getQueue(), DEAD_LETTER_EXCHANGE, "", new HashMap<String, Object>());

        // 消费正常队列
        channel.basicConsume(declareOk.getQueue(), false, "consume alive", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("adamhand.order.alive..............");
                    System.out.println(new String(body, "UTF-8"));
                    System.out.println("adamhand.order.alive将消息拒绝");
                    channel.basicNack(envelope.getDeliveryTag(), false,false);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });

        // 消费死信队列
        channel.basicConsume(declareOk2.getQueue(), false, "consumer dead", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("adamhand.order.dead..............");
                    System.out.println(new String(body, "UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });
    }
}
