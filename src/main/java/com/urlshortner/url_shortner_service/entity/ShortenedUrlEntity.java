package com.urlshortner.url_shortner_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table(name = "shortened_url")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ShortenedUrlEntity {
    @Id
    private UUID id;
    private String clientIdentifier;
    private String customerId;
    private String shortcode;
    private String redirectUrl;
    private Boolean isSingleAccess;
    private LocalDateTime expiresAt;
    private int clickCount;
    private LocalDateTime updated;
    private LocalDateTime created;
}
