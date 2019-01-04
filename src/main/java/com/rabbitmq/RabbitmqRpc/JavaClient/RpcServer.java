package com.rabbitmq.RabbitmqRpc.JavaClient;

import com.rabbitmq.Dependability.ConsumerDependability.JavaClient.RpcMethod;
import com.rabbitmq.Exception.ChannelUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

public class RpcServer {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String QUEUE_NAME = "adamhand.order.add";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE = "UTF-8";

    public static void main(String[] args) throws IOException {
        final Channel channel = ChannelUtils.getChannelInstance("Rpc Server");

        channel.queueDeclare(QUEUE_NAME, true, false, false,
                new HashMap<String, Object>());
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, false,
                new HashMap<String, Object>());

        channel.basicConsume(QUEUE_NAME, true, "rpc server", new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String replyTo = properties.getReplyTo();
                System.out.println(replyTo);
                String correlationId = properties.getCorrelationId();

                System.out.println("received rpc request..........");
                System.out.println(consumerTag);
                System.out.println("consumer properties: "+properties);
                System.out.println("consumer body: "+new String(body));

                try {
                    String id = RpcMethod.addOrder(new String(body));
                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                            .builder()
                            .deliveryMode(2)
                            .contentType(CONTENT_TYPE)
                            .correlationId(correlationId)
                            .build();
                    channel.basicPublish("", replyTo, basicProperties, id.getBytes());  //发送到默认交换器
                    System.out.println("rpc has successfully returned");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
