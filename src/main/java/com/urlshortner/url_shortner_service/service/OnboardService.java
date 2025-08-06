package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.model.OnboardRequest;
import com.urlshortner.url_shortner_service.model.OnboardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OnboardService {
    public OnboardResponse onboardClient(OnboardRequest onboardRequest){
        return OnboardResponse.builder()
                .clientIdentifier(onboardRequest.getClientIdentifier())
                .apiKey(UUID.randomUUID().toString())
                .build();
    }
}
