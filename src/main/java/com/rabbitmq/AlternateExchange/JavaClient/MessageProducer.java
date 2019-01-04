package com.rabbitmq.AlternateExchange.JavaClient;

import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MessageProducer {
    private static final String EXCHANGE_NAME = "adamhand.order.normal";
    private static final String AE_EXCHANGE_NAME = "adamhand.order.ae";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannelInstance("message producer");

        /**
         * 声明一个正常交换器并配置AE
         */
        Map<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", AE_EXCHANGE_NAME);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false, map);

        /**
         * 声明一个AE_EXCHANGE
         */
        channel.exchangeDeclare(AE_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true, false, false,
                new HashMap<String, Object>());

        /**
         * 发送一条不能正确路由的消息
         */
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentType("UTF-8")
                .build();
        channel.basicPublish(EXCHANGE_NAME, "addXXX", false, basicProperties, "order information".getBytes());
    }
}
