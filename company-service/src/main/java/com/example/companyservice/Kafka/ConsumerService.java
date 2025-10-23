package com.example.companyservice.Kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "company-registered", groupId = "company-group")
    public void consumeUserRegisteredEvent(String message) {
        System.out.println("Received from Kafka [company-registered]: " + message);

    }
}
