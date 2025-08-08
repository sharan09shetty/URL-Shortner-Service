package com.urlshortner.url_shortner_service.controller;

import com.urlshortner.url_shortner_service.model.ShortenUrlRequest;
import com.urlshortner.url_shortner_service.model.ShortenUrlResponse;
import com.urlshortner.url_shortner_service.service.ShortnerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shorten/url")
@AllArgsConstructor
public class ShortenUrlController {

    private final ShortnerService shortnerService;

    @PostMapping
    public ResponseEntity<ShortenUrlResponse> getShortenedUrl(@RequestParam String clientIdentifier, @RequestParam String apiKey, @RequestBody ShortenUrlRequest shortenUrlRequest) {
        try {
            validateRequest(clientIdentifier, apiKey, shortenUrlRequest);
            ShortenUrlResponse shortenUrlResponse = shortnerService.getShortenedUrl(clientIdentifier, apiKey, shortenUrlRequest);
            return ResponseEntity.ok(shortenUrlResponse);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(ShortenUrlResponse.builder().errorMessage(iae.getMessage()).build());
        } catch (Exception e) {
            log.error("Error generating short URL : ", e);
            return ResponseEntity.internalServerError().body(ShortenUrlResponse.builder().errorMessage(e.getMessage()).build());
        }
    }

    public void validateRequest(String clientIdentifier, String apiKey, ShortenUrlRequest shortenUrlRequest) {
        if (isNullOrEmpty(clientIdentifier) || isNullOrEmpty(apiKey)) {
            throw new IllegalArgumentException("ClientIdentifier or ApiKey cannot be null or empty");
        }
        if (isNullOrEmpty(shortenUrlRequest.getRedirectUrl())) {
            throw new IllegalArgumentException("Redirect URL cannot be null or empty");
        }
        if (shortenUrlRequest.getIsSingleAccess() == null) {
            shortenUrlRequest.setIsSingleAccess(false);
        }
        if (shortenUrlRequest.getExpirationMinutes() == null) {
            shortenUrlRequest.setExpirationMinutes(-1);
        }
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
