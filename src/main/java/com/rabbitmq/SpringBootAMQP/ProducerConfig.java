package com.rabbitmq.SpringBootAMQP;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ProducerConfig {
    private static final String EXCHANGE_NAME = "adamhand.order";
    @Bean
    public Exchange exchange(){
        return new DirectExchange(EXCHANGE_NAME, true, false,
                new HashMap<String, Object>());
    }
}
