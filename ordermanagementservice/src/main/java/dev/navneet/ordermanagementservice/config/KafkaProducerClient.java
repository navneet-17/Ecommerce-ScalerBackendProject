package dev.navneet.ordermanagementservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;

@Configuration
public class KafkaProducerClient {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(value = Exception.class, maxAttempts = 5)
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}