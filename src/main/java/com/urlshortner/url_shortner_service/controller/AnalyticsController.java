package com.urlshortner.url_shortner_service.controller;

import com.urlshortner.url_shortner_service.model.ShortcodeDetails;
import com.urlshortner.url_shortner_service.service.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/link")
@AllArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping(value = "/{shortcode}")
    public ShortcodeDetails getDetailsForShortcode(@RequestParam String clientIdentifier, @RequestParam String apiKey, @PathVariable String shortcode){
        return analyticsService.getDetailsForShortcode(clientIdentifier, apiKey, shortcode);
    }
}
