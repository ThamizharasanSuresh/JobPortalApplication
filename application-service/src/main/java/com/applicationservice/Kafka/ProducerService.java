package com.applicationservice.Kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private static final String TOPIC = "application-submitted";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendApplicationSubmittedEvent(String applicantName) {
        String message = "New application applied by: " + applicantName;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Sent Kafka event: " + message);
    }
}
