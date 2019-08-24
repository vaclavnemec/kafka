package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @KafkaListener(topics = "my-test-topic", groupId = "consumer-group-1")
    public void listen(String message) {
        System.out.println("THIS MESSAGE WAS CONSUMED BY THE LISTENER: " + message);
    }

}
