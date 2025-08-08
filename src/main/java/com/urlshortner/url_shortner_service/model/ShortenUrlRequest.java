package com.urlshortner.url_shortner_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortenUrlRequest {
    private String customerId;
    private String redirectUrl;
    private Boolean isSingleAccess;
    private Integer expirationMinutes;
}
