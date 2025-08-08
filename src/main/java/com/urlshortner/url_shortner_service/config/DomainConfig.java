package com.urlshortner.url_shortner_service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class DomainConfig {
    @Value("${application.domain.name}")
    private String domainName;
}
