package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.entity.ClientEntity;
import com.urlshortner.url_shortner_service.model.OnboardRequest;
import com.urlshortner.url_shortner_service.model.OnboardResponse;
import com.urlshortner.url_shortner_service.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OnboardService {
    private final ClientRepository clientRepository;

    public OnboardResponse onboardClient(OnboardRequest onboardRequest) {
        ClientEntity clientEntity = clientRepository.findByClientIdentifier(onboardRequest.getClientIdentifier().toLowerCase());
        if (clientEntity == null) {
            ClientEntity newclientEntity = this.buildClientEntity(onboardRequest);
            clientRepository.save(newclientEntity);
            return OnboardResponse.builder()
                    .apiKey(newclientEntity.getApiKey())
                    .clientIdentifier(newclientEntity.getClientIdentifier())
                    .build();
        }
        return OnboardResponse.builder()
                .errorMessage("Try with a different client-identifier.")
                .build();
    }

    private ClientEntity buildClientEntity(OnboardRequest onboardRequest) {
        return ClientEntity.builder()
                .id(UUID.randomUUID())
                .name(onboardRequest.getClientName())
                .apiKey(UUID.randomUUID().toString())
                .clientIdentifier(onboardRequest.getClientIdentifier().toLowerCase())
                .created(LocalDateTime.now())
                .build();
    }
}
