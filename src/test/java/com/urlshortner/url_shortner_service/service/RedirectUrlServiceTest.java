package com.urlshortner.url_shortner_service.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.urlshortner.url_shortner_service.entity.ShortenedUrlEntity;
import com.urlshortner.url_shortner_service.exception.LinkExpiredException;
import com.urlshortner.url_shortner_service.exception.ResourceNotFoundException;
import com.urlshortner.url_shortner_service.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RedirectUrlServiceTest {

    @InjectMocks
    private RedirectUrlService redirectUrlService;

    @Mock
    private ShortenedUrlRepository shortenedUrlRepository;

    public final String EXPIRYMESSAGE = "This link has expired :(.";
    public final String NOTFOUNDMESSAGE = "OOPS! NOT FOUND :(";

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRedirectUrl(){
        String shortCode = NanoIdUtils.randomNanoId();
        ShortenedUrlEntity shortenedUrlEntity = ShortenedUrlEntity.builder()
                .redirectUrl("https://www.github.com")
                .isSingleAccess(false)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .clickCount(0)
                .build();
        when(shortenedUrlRepository.findByShortcode(anyString())).thenReturn(shortenedUrlEntity);
        String redirectUrl = redirectUrlService.getRedirectUrl(shortCode);
        assertEquals("https://www.github.com", redirectUrl);
        assertEquals(1, shortenedUrlEntity.getClickCount());
    }

    @Test
    public void shouldRedirectUrlWithHttp(){
        String shortCode = NanoIdUtils.randomNanoId();
        ShortenedUrlEntity shortenedUrlEntity = ShortenedUrlEntity.builder()
                .redirectUrl("www.github.com")
                .isSingleAccess(false)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .clickCount(0)
                .build();
        when(shortenedUrlRepository.findByShortcode(anyString())).thenReturn(shortenedUrlEntity);
        String redirectUrl = redirectUrlService.getRedirectUrl(shortCode);
        assertEquals("https://www.github.com", redirectUrl);
        assertEquals(1, shortenedUrlEntity.getClickCount());
    }

    @Test
    public void shouldExpireUrlForSingleAccess(){
        String shortCode = NanoIdUtils.randomNanoId();
        ShortenedUrlEntity shortenedUrlEntity = ShortenedUrlEntity.builder()
                .redirectUrl("https://www.github.com")
                .isSingleAccess(true)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .clickCount(0)
                .build();
        when(shortenedUrlRepository.findByShortcode(anyString())).thenReturn(shortenedUrlEntity);
        String redirectUrl = redirectUrlService.getRedirectUrl(shortCode);
        assertTrue(shortenedUrlEntity.getExpiresAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals("https://www.github.com", redirectUrl);
        assertEquals(1, shortenedUrlEntity.getClickCount());
    }

    @Test
    public void shouldNotRedirectUrlForExpiredUrl(){
        String shortCode = NanoIdUtils.randomNanoId();
        ShortenedUrlEntity shortenedUrlEntity = ShortenedUrlEntity.builder()
                .redirectUrl("https://www.github.com")
                .isSingleAccess(false)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .clickCount(0)
                .build();
        when(shortenedUrlRepository.findByShortcode(anyString())).thenReturn(shortenedUrlEntity);
        LinkExpiredException exception = assertThrows(
                LinkExpiredException.class,
                () -> redirectUrlService.getRedirectUrl(shortCode)
        );
        assertEquals(EXPIRYMESSAGE, exception.getMessage());
        assertEquals(0, shortenedUrlEntity.getClickCount());
    }

    @Test
    public void shouldNotRedirectUrlForUrlNotFound(){
        String shortCode = NanoIdUtils.randomNanoId();
        when(shortenedUrlRepository.findByShortcode(anyString())).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> redirectUrlService.getRedirectUrl(shortCode)
        );
        assertEquals(NOTFOUNDMESSAGE, exception.getMessage());
    }
}
