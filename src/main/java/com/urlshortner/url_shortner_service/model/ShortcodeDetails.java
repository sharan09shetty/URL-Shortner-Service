package com.urlshortner.url_shortner_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortcodeDetails {
    private String customerId;
    private String redirectUrl;
    private Boolean isSingleAccess;
    private Boolean isExpired;
    private LocalDateTime expiresOn;
    private Integer clickCount;
    private LocalDateTime lastClickedOn;
    private String message;
}
