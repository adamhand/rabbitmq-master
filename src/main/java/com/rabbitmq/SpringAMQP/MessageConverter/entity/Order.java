package com.rabbitmq.SpringAMQP.MessageConverter.entity;

import lombok.Data;

@Data
public class Order {
    private String id;
    private float price;

    public Order() {
    }

    public Order(String id, float price) {
        this.id = id;
        this.price = price;
    }
}
