package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @KafkaListener(topics = "test-topic")
    public void listen(String message) {
        System.out.println("THIS MESSAGE WAS CONSUMED BY THE LISTENER: " + message);
    }

}
