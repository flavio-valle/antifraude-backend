package com.fiap.challenge.quod.antifraude_backend.dto;

import java.time.Instant;

public class FraudEvent {
    private String usuarioId;
    private String evento;       // "FRAUDE" ou "SUCESSO"
    private String categoria;    // "FACIAL", "DIGITAL" ou "DOCUMENTO"
    private Instant timestamp;

    public FraudEvent() {}

    public FraudEvent(String usuarioId, String evento, String categoria, Instant timestamp) {
        this.usuarioId = usuarioId;
        this.evento = evento;
        this.categoria = categoria;
        this.timestamp = timestamp;
    }

    // getters & setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}