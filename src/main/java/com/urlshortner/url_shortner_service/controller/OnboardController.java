package com.urlshortner.url_shortner_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OnboardController {
    @GetMapping(value="/get")
    public String getMessage(){
        return "Hello World";
    }
}
