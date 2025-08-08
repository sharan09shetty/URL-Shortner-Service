package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.config.DomainConfig;
import com.urlshortner.url_shortner_service.entity.ClientEntity;
import com.urlshortner.url_shortner_service.entity.ShortenedUrlEntity;
import com.urlshortner.url_shortner_service.model.ShortenUrlRequest;
import com.urlshortner.url_shortner_service.model.ShortenUrlResponse;
import com.urlshortner.url_shortner_service.repository.ClientRepository;
import com.urlshortner.url_shortner_service.repository.ShortenedUrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShortnerService {
    private final DomainConfig domainConfig;
    private final ClientRepository clientRepository;
    private final ShortenedUrlRepository shortenedUrlRepository;

    public ShortenUrlResponse getShortenedUrl(String clientIdentifier, String apiKey, ShortenUrlRequest shortenUrlRequest) {
        if (!isAuthorizedClient(clientIdentifier, apiKey)) {
            return ShortenUrlResponse.builder().errorMessage("Incorrect clientIdentifier or ApiKey").build();
        }
        String shortCode = NanoIdUtils.randomNanoId();
        String domainName = domainConfig.getDomainName();
        String shortUrl = domainName.endsWith("/") ? domainName + shortCode : domainName + "/" + shortCode;
        ShortenedUrlEntity shortenedUrlEntity = buildShortenedUrlEntity(clientIdentifier, shortenUrlRequest, shortCode);
        shortenedUrlEntity.setExpiresAt(calculateLinkExpiryTime(shortenUrlRequest.getExpirationMinutes()));
        shortenedUrlRepository.save(shortenedUrlEntity);
        return ShortenUrlResponse.builder()
                .customerId(shortenUrlRequest.getCustomerId())
                .shortenedUrl(shortUrl)
                .build();
    }

    private LocalDateTime calculateLinkExpiryTime(Integer expiryMinutes) {
        return expiryMinutes == -1 ? LocalDateTime.now().plusYears(1) : LocalDateTime.now().plusMinutes(expiryMinutes);
    }

    private ShortenedUrlEntity buildShortenedUrlEntity(String clientIdentifier, ShortenUrlRequest shortenUrlRequest, String nanoId) {
        return ShortenedUrlEntity.builder()
                .id(UUID.randomUUID())
                .customerId(shortenUrlRequest.getCustomerId())
                .clientIdentifier(clientIdentifier)
                .redirectUrl(shortenUrlRequest.getRedirectUrl())
                .shortcode(nanoId)
                .isSingleAccess(shortenUrlRequest.getIsSingleAccess())
                .clickCount(0)
                .updated(LocalDateTime.now())
                .created(LocalDateTime.now())
                .build();
    }


    public boolean isAuthorizedClient(String clientIdentifier, String apiKey) {
        ClientEntity clientEntity = clientRepository.findByClientIdentifier(clientIdentifier);
        if (clientEntity == null) {
            return false;
        }
        return clientEntity.getApiKey().equals(apiKey);
    }
}
