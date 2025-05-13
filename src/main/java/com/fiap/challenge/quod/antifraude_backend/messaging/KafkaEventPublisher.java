package com.fiap.challenge.quod.antifraude_backend.messaging;

import com.fiap.challenge.quod.antifraude_backend.dto.FraudEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafka;
    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publish(FraudEvent event) {
        kafka.send(topic, event.getUsuarioId(), event);
    }
}
