package com.urlshortner.url_shortner_service.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ShortenedUrlEntity {
    @Id
    private UUID id;
    private String clientIdentifier;
    private String shortenedUrl;
    private String redirectUrl;
    private Boolean isSingleAccess;
    private LocalDateTime expiresAt;
    private int clickCount;
    private LocalDateTime updated;
    private LocalDateTime created;
}
