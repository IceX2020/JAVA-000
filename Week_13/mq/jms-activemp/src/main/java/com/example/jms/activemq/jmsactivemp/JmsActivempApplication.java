package com.example.jms.activemq.jmsactivemp;

import com.example.jms.activemq.jmsactivemp.jms.JmsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lw
 */
@SpringBootApplication
@EnableJms
@Slf4j
public class JmsActivempApplication implements ApplicationRunner {

    @Autowired
    private JmsProducer producer;

    public static void main(String[] args) {
        SpringApplication.run(JmsActivempApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        String topic = "activeTest";
        Map<String, String> message = new HashMap<>(1);
        message.put("test", "test");
        log.info("send message to topic " + topic + " :: " + message);
        producer.sendMessage(topic, message);
    }
}
