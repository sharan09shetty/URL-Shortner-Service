package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.entity.ShortenedUrlEntity;
import com.urlshortner.url_shortner_service.model.ShortcodeDetails;
import com.urlshortner.url_shortner_service.repository.ShortenedUrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AnalyticsService {
    private final ShortnerService shortnerService;
    private final ShortenedUrlRepository shortenedUrlRepository;
    public ShortcodeDetails getDetailsForShortcode(String clientIdentifier, String apiKey, String shortcode){
        if (!shortnerService.isAuthorizedClient(clientIdentifier, apiKey)) {
            return ShortcodeDetails.builder().message("Incorrect clientIdentifier or ApiKey").build();
        }
        ShortenedUrlEntity shortenedUrlEntity = shortenedUrlRepository.findByShortcodeAndClientIdentifier(shortcode, clientIdentifier);
        if(shortenedUrlEntity == null){
            return ShortcodeDetails.builder().message("No Details found for the provided Shortcode.").build();
        }
        Boolean isExpired = shortenedUrlEntity.getExpiresAt().isBefore(LocalDateTime.now());
        LocalDateTime lastClickedOn = null;
        if(shortenedUrlEntity.getClickCount() > 0){
            lastClickedOn = shortenedUrlEntity.getUpdated();
        }
        return this.buildShortcodeDetails(shortenedUrlEntity, isExpired, lastClickedOn);
    }

    private ShortcodeDetails buildShortcodeDetails(ShortenedUrlEntity shortenedUrlEntity, Boolean isExpired, LocalDateTime lastClickedOn){
        return ShortcodeDetails.builder()
                .customerId(shortenedUrlEntity.getCustomerId())
                .redirectUrl(shortenedUrlEntity.getRedirectUrl())
                .isSingleAccess(shortenedUrlEntity.getIsSingleAccess())
                .isExpired(isExpired)
                .expiresOn(isExpired ? null : shortenedUrlEntity.getExpiresAt())
                .clickCount(shortenedUrlEntity.getClickCount())
                .lastClickedOn(lastClickedOn)
                .build();
    }
}
