package com.rabbitmq.RabbitmqRpc.SpringAMQP;

public class RpcMethod {
    public static String addOrder(String id){
        try {
            System.out.println(id);
            System.out.println("order has been added");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
