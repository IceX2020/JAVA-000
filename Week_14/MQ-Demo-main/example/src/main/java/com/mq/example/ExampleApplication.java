package com.mq.example;

import com.mq.core.core.consumer.Consumer;
import com.mq.core.core.consumer.HttpConsumer;
import com.mq.core.core.producer.HttpProducer;
import com.mq.core.core.producer.Producer;
import com.mq.core.core.producer.WebsocketProducer;

import java.util.HashMap;
import java.util.Map;

public class ExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        int messageAmount = 100000;
        String topic = "testTopic";
        int getRate = 10000;

//        startHttpMQProducer(messageAmount);
        startWebsocketMqProducer(messageAmount, topic);

        startHttpMQConsumer(messageAmount, topic, getRate);
    }

    private static void startWebsocketMqProducer(int messageAmount, String topic) {
        Producer producer = new WebsocketProducer("ws://localhost:8080/producer");

        int amount = messageAmount;

        System.out.println("start producer test");
        long start = System.currentTimeMillis();

        while (amount > 0) {
            producer.send(topic, "producerMessage");
            amount -= 1;
        }

        System.out.println("Producer " + messageAmount + " messages spend time : " +
                (System.currentTimeMillis() - start) + " ms ");
    }

    private static void startHttpMQConsumer(int messageAmount, String topic, int getRate) {
        Map<String, Object> properties = new HashMap<>(1);
        properties.put("url", "http://localhost:8080");
        Consumer consumer = new HttpConsumer(properties);
        int amount = messageAmount;

        System.out.println("Start consumer test");
        long start = System.currentTimeMillis();

        while (amount > 0) {
           amount -= consumer.poll(topic, getRate).size();
        }

        System.out.println("Consumer " + messageAmount + " messages spend time : " + (System.currentTimeMillis() - start) + " " +
                "ms");
    }

    private static void startHttpMQProducer(int messageAmount) {
        Map<String, Object> properties = new HashMap<>(1);
        properties.put("url", "http://localhost:8080");
        Producer producer = new HttpProducer(properties);
        String topic = "testTopic";

        System.out.println("start producer test");
        long start = System.currentTimeMillis();

        for(int i = 0; i < messageAmount; i++) {
            producer.send(topic, String.valueOf(i));
        }

        System.out.println("Producer " + messageAmount + " messages spend time : " +
                (System.currentTimeMillis() - start) + " ms ");
    }
}
