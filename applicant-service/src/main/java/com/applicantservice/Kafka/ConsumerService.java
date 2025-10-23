package com.applicantservice.Kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class  ConsumerService {

    @KafkaListener(topics = "application-submitted", groupId = "applicant-group")
    public void consumeApplicationSubmittedEvent(String message) {
        System.out.println("Received from Kafka [application-submitted]: " + message);
    }

    @KafkaListener(topics = "user-registered", groupId = "applicant-group")
    public void consumeUserRegisteredEvent(String message) {
        System.out.println("Received from Kafka [user-registered]: " + message);

    }
}
