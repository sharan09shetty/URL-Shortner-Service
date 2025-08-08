package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.entity.ShortenedUrlEntity;
import com.urlshortner.url_shortner_service.repository.ShortenedUrlRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.urlshortner.url_shortner_service.exception.ResourceNotFoundException;
import com.urlshortner.url_shortner_service.exception.LinkExpiredException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RedirectUrlService {
    private final ShortenedUrlRepository shortenedUrlRepository;

    public final String EXPIRYMESSAGE = "This link has expired :(.";
    public final String NOTFOUNDMESSAGE = "OOPS! NOT FOUND :(";

    @Transactional
    public String getRedirectUrl(String shortcode) {
        ShortenedUrlEntity shortenedUrlEntity = shortenedUrlRepository.findByShortcode(shortcode);
        if (shortenedUrlEntity == null) {
            throw new ResourceNotFoundException(NOTFOUNDMESSAGE);
        }
        if (shortenedUrlEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new LinkExpiredException(EXPIRYMESSAGE);
        }
        if (shortenedUrlEntity.getIsSingleAccess()) {
            shortenedUrlEntity.setExpiresAt(LocalDateTime.now());
        }
        shortenedUrlEntity.setClickCount(shortenedUrlEntity.getClickCount() + 1);
        shortenedUrlEntity.setUpdated(LocalDateTime.now());
        return normalizeUrl(shortenedUrlEntity.getRedirectUrl());
    }

    private String normalizeUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return "https://" + url;
    }
}
