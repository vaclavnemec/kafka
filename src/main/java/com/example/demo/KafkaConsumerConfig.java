package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    
//    @Bean
//  	public ConcurrentKafkaListenerContainerFactory<String, String> myKafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
//  		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//  		factory.setConsumerFactory(consumerFactory);
//  		factory.setConcurrency(4);
//  		return factory;
//  	}
}
