package com.urlshortner.url_shortner_service.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.urlshortner.url_shortner_service.config.DomainConfig;
import com.urlshortner.url_shortner_service.entity.ClientEntity;
import com.urlshortner.url_shortner_service.model.ShortenUrlRequest;
import com.urlshortner.url_shortner_service.model.ShortenUrlResponse;
import com.urlshortner.url_shortner_service.repository.ClientRepository;
import com.urlshortner.url_shortner_service.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ShortnerServiceTest {
    @InjectMocks
    private ShortnerService shortnerService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DomainConfig domainConfig;

    @Mock
    private ShortenedUrlRepository shortenedUrlRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldShortenUrl(){
        ShortenUrlRequest shortenUrlRequest = ShortenUrlRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .redirectUrl("www.github.com")
                .expirationMinutes(10)
                .isSingleAccess(false)
                .build();
        String clientIdentifier = "test";
        String apiKey = UUID.randomUUID().toString();
        String shortCode = NanoIdUtils.randomNanoId();
        try (MockedStatic<NanoIdUtils> mockedNanoId = mockStatic(NanoIdUtils.class)) {
            mockedNanoId.when(NanoIdUtils::randomNanoId).thenReturn(shortCode);

            when(domainConfig.getDomainName()).thenReturn("localhost:8090");

            ClientEntity clientEntity = ClientEntity.builder().apiKey(apiKey).build();
            when(clientRepository.findByClientIdentifier(anyString())).thenReturn(clientEntity);

            ShortenUrlResponse shortenUrlResponse = shortnerService.getShortenedUrl(clientIdentifier, apiKey, shortenUrlRequest);

            assertEquals("localhost:8090/" + shortCode, shortenUrlResponse.getShortenedUrl());
            assertNull(shortenUrlResponse.getErrorMessage());
        }
    }

    @Test
    public void shouldReturnErrorMessageForUnAuthorizedClient(){
        ShortenUrlRequest shortenUrlRequest = ShortenUrlRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .redirectUrl("www.github.com")
                .expirationMinutes(10)
                .isSingleAccess(false)
                .build();
        String clientIdentifier = "test";
        String apiKey = UUID.randomUUID().toString();
        when(clientRepository.findByClientIdentifier(anyString())).thenReturn(null);
        ShortenUrlResponse shortenUrlResponse = shortnerService.getShortenedUrl(clientIdentifier, apiKey, shortenUrlRequest);
        assertEquals("Incorrect clientIdentifier or ApiKey", shortenUrlResponse.getErrorMessage());
        assertNull(shortenUrlResponse.getShortenedUrl());
    }

}
