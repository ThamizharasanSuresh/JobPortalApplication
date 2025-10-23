package com.applicationservice.Kafka;



import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "job-created", groupId = "application-group")
    public void consumeJobCreatedEvent(String message) {
        System.out.println("Received from Kafka [job-created]: " + message);
    }
}

