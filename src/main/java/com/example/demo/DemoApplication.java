package com.example.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("test-topic", 10, (short) 1);
    }

    @Bean
    public CommandLineRunner generateMessages(KafkaTemplate kafkaTemplate) {
        return args -> {
            IntStream.iterate(0, i -> i + 1).forEach(
                    data -> {
                    	System.out.println("Sending: " + data);
                        kafkaTemplate.send("test-topic", String.valueOf(data));
						try {
							System.out.println("Sleeping");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
            );
        };
    }

}
