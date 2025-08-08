package com.urlshortner.url_shortner_service.controller;

import com.urlshortner.url_shortner_service.exception.LinkExpiredException;
import com.urlshortner.url_shortner_service.exception.ResourceNotFoundException;
import com.urlshortner.url_shortner_service.service.RedirectUrlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/{shortcode}")
@AllArgsConstructor
public class RedirectUrlController {

    private final RedirectUrlService shortnerService;

    @GetMapping
    public ResponseEntity<?> getRedirectUrl(@PathVariable String shortcode) {
        try {
            String redirectUrl = shortnerService.getRedirectUrl(shortcode);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rnfe.getMessage());
        } catch (LinkExpiredException lee) {
            return ResponseEntity.status(HttpStatus.GONE).body(lee.getMessage());
        } catch (Exception e) {
            log.error("Exception found while redirecting URL : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
