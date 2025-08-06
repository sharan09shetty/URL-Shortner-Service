package com.urlshortner.url_shortner_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name="client")
public class ClientEntity {
    @Id
    private UUID id;
    private String name;
    private String apiKey;
    private String clientIdentifier;
    private LocalDateTime created;
}
