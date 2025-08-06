package com.urlshortner.url_shortner_service.controller;

import com.urlshortner.url_shortner_service.model.OnboardRequest;
import com.urlshortner.url_shortner_service.model.OnboardResponse;
import com.urlshortner.url_shortner_service.service.OnboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboard")
@AllArgsConstructor
public class OnboardController {

    private final OnboardService onboardService;

    @PostMapping(consumes = {"application/json"},
    produces= {"application/json"})
    public ResponseEntity<OnboardResponse> clientOnboard(@RequestBody OnboardRequest onboardRequest){
        return ResponseEntity.ok(onboardService.onboardClient(onboardRequest));
    }
}
