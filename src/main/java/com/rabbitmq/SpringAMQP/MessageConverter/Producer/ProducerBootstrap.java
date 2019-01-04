package com.rabbitmq.SpringAMQP.MessageConverter.Producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.SpringAMQP.MessageConverter.entity.Order;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan(basePackages = "com.rabbitmq.SpringAMQP.MessageConverter.Producer")
public class ProducerBootstrap {
    private static final String EXCHANGE_NAME = "adamhand.order";
    private static final String ROUTING_KEY = "add";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_IMAGE = "image/jpg";

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProducerBootstrap.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        /**
         * 声明交换机
         */
        rabbitAdmin.declareExchange(
                new DirectExchange(EXCHANGE_NAME, true, false,
                new HashMap<String, Object>()));

        /**
         * 发送String
         */
        sendString(rabbitTemplate);
        /**
         * 发送单个对象JSON
         */
        sendSingle(rabbitTemplate);
        /**
         * 发送List集合JSON
         */
        sendList(rabbitTemplate);
        /**
         * 发送Map集合JSON
         */
        sendMap(rabbitTemplate);
        /**
         * 发送图片
         */
        sendImage(rabbitTemplate);
    }

    public static void sendString(RabbitTemplate rabbitTemplate){
        MessageProperties msgProperties = new MessageProperties();
        msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties.setContentType(CONTENT_TYPE_TEXT);
        Message msg = new Message("order information".getBytes(), msgProperties);

        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, msg);
    }

    public static void sendSingle(RabbitTemplate rabbitTemplate) throws JsonProcessingException {
        Order order = new Order("OD00000000001", (float) 5555.5555);
        ObjectMapper mapper = new ObjectMapper();

        MessageProperties msgProperties = new MessageProperties();
        msgProperties.getHeaders().put("_TypeId_", "order");
        msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties.setContentType(CONTENT_TYPE_JSON);
        Message msg = new Message(mapper.writeValueAsString(order).getBytes(), msgProperties);

        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, msg);
    }

    public static void sendList(RabbitTemplate rabbitTemplate) throws JsonProcessingException {
        Order order1 = new Order("OD00000000001", (float) 5555.5555);
        Order order2 = new Order("OD00000000002", (float) 3333.3333);
        List<Order> orderList = Arrays.asList(order1, order2);

        ObjectMapper mapper = new ObjectMapper();

        MessageProperties msgProperties = new MessageProperties();
        msgProperties.getHeaders().put("__TypeId__", "java.util.List");
        msgProperties.getHeaders().put("__ContentTypeId__", "order");
        msgProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        msgProperties.setContentType(CONTENT_TYPE_JSON);
        Message msg = new Message(mapper.writeValueAsString(orderList).getBytes(), msgProperties);

        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, msg);
    }

    public static void sendMap(RabbitTemplate rabbitTemplate) throws Exception {
        Order order1 = new Order("OD0000001", (float)1111.1111);
        Order order2 = new Order("OD0000002", (float)2222.2222);
        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put(order1.getId(), order1);
        orderMap.put(order2.getId(), order2);

        ObjectMapper objectMapper = new ObjectMapper();
        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("__TypeId__", "java.util.Map");
        messageProperties.getHeaders().put("__KeyTypeId__", "java.lang.String");
        messageProperties.getHeaders().put("__ContentTypeId__", "order");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        Message message = new Message(objectMapper.writeValueAsString(orderMap).getBytes(), messageProperties);

        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, message);
    }

    public static void sendImage(RabbitTemplate rabbitTemplate) throws Exception {
        File file = new File(System.getProperty("user.dir")+"\\naruto.jpg"); //程序运行之前该图片要存在
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        int length;
        byte[] b = new byte[1024];
        while ((length = fileInputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, length);
        }
        fileInputStream.close();
        byteArrayOutputStream.close();
        byte[] buffer = byteArrayOutputStream.toByteArray();

        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("_extName", "jpg");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType(CONTENT_TYPE_IMAGE);
        Message message = new Message(buffer, messageProperties);

        rabbitTemplate.send(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
