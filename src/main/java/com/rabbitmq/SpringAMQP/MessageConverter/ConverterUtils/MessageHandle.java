package com.rabbitmq.SpringAMQP.MessageConverter.ConverterUtils;

import com.rabbitmq.SpringAMQP.MessageConverter.entity.Order;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MessageHandle {
    public void add(byte[] data){
        System.out.println("use byte[] to handle");
        System.out.println(data.toString());
    }

    public void add(String msg){
        System.out.println("use String to handle");
        System.out.println(msg);
    }

    public void add(File file){
        System.out.println("use File to handle");
        System.out.println(file.length() +" "+ file.getName()+ " " + file.getAbsolutePath());
    }

    public void add(Order order){
        System.out.println("use Order to handle");
        System.out.println(order.getId()+" "+ order.getPrice());
    }

    public void add(List<Order> list){
        System.out.println("use list to handle");
        System.out.println(list.size());
        for (Order o : list){
            System.out.println(o.getId()+" "+o.getPrice());
        }
    }

    public void add(Map<String, Order> map){
        System.out.println("use Map to handle");
        for(Map.Entry<String, Order> entry : map.entrySet()){
            System.out.println(entry.getKey() +" "+ entry.getValue());
        }
    }
}
