package com.urlshortner.url_shortner_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnboardResponse {
    private String clientIdentifier;
    private String apiKey;
    private String errorMessage;
}
