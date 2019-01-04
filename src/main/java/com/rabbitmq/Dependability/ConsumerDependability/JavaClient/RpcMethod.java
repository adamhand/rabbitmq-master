package com.rabbitmq.Dependability.ConsumerDependability.JavaClient;

public class RpcMethod {
    public static String addOrder(String id){
        try {
            System.out.println("order has been added");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
