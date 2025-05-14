package com.fiap.challenge.quod.antifraude_backend.interfaces;

import com.fiap.challenge.quod.antifraude_backend.dto.FraudEvent;

@FunctionalInterface
public interface EventPublisherInterface {
    void publish(FraudEvent evt);
}
