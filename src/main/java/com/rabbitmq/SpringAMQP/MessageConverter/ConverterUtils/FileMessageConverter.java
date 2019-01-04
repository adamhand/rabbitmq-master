package com.rabbitmq.SpringAMQP.MessageConverter.ConverterUtils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String exName = (String) message.getMessageProperties().getHeaders().get("_extName");
        byte[] data = message.getBody();

        String fileName = UUID.randomUUID().toString();
        String filePath = System.getProperty("java.io.temdir")+ fileName +"." + exName;
        File temFile = new File(filePath);

        try {
            FileCopyUtils.copy(data, temFile);
        } catch (IOException e) {
            throw new MessageConversionException("file message convert fails", e);
        }

        return temFile;
    }
}
