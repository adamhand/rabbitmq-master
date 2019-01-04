package com.rabbitmq.SpringAMQP.Annotation.Consumer;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 自定义消费者消息处理器类
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "adamhand.order.add",
        durable = "true", autoDelete = "false", exclusive = "false"),
        exchange = @Exchange(name = "adamhand.order"))})
public class MessageHandle {
    @RabbitHandler
    public void add(byte[] data){
        System.out.println("use byte[] to handle");
        try {
            System.out.println(new String(data, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
