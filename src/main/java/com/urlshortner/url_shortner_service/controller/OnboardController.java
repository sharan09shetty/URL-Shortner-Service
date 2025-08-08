package com.urlshortner.url_shortner_service.controller;

import com.urlshortner.url_shortner_service.model.OnboardRequest;
import com.urlshortner.url_shortner_service.model.OnboardResponse;
import com.urlshortner.url_shortner_service.service.OnboardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/onboard")
@AllArgsConstructor
public class OnboardController {

    private final OnboardService onboardService;

    @PostMapping(consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<OnboardResponse> clientOnboard(@RequestBody OnboardRequest onboardRequest) {
        try {
            validateRequest(onboardRequest);
            return ResponseEntity.ok(onboardService.onboardClient(onboardRequest));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(OnboardResponse.builder().errorMessage(iae.getMessage()).build());
        } catch (Exception e) {
            log.error("Unexpected error : {}", e.getMessage());
            return ResponseEntity.internalServerError().body(OnboardResponse.builder().errorMessage("Unexpected error during onboarding :" + e.getMessage()).build());
        }
    }

    public void validateRequest(OnboardRequest onboardRequest) {
        if (onboardRequest == null || isNullOrEmpty(onboardRequest.getClientName()) || isNullOrEmpty(onboardRequest.getClientIdentifier())) {
            throw new IllegalArgumentException("clientName or clientIdentifier cannot be null");
        }
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
