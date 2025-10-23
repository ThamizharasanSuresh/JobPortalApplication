package com.jobservice.Kafka;



import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private static final String TOPIC = "job-created";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendJobCreatedEvent(String jobTitle,String companyName) {
        String message = "New job created by " + companyName + " Role is " + jobTitle;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Sent Kafka event: " + message);
    }
}
