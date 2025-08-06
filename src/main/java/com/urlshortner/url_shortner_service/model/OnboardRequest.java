package com.urlshortner.url_shortner_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnboardRequest {
    private String clientName;
    private String clientIdentifier;
}
