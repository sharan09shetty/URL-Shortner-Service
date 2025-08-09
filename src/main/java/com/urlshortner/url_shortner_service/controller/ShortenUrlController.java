package com.urlshortner.url_shortner_service.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.urlshortner.url_shortner_service.model.ShortenUrlRequest;
import com.urlshortner.url_shortner_service.model.ShortenUrlResponse;
import com.urlshortner.url_shortner_service.service.ShortnerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/shorten")
@AllArgsConstructor
public class ShortenUrlController {
    private final ShortnerService shortnerService;

    @PostMapping(value = "/url")
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

    @PostMapping(value = "/qrcode")
    public ResponseEntity<byte[]> getShortenedUrlQRCode(@RequestParam String clientIdentifier, @RequestParam String apiKey, @RequestBody ShortenUrlRequest shortenUrlRequest) {
        try {
            validateRequest(clientIdentifier, apiKey, shortenUrlRequest);
            ShortenUrlResponse shortenUrlResponse = shortnerService.getShortenedUrl(clientIdentifier, apiKey, shortenUrlRequest);
            if (shortenUrlResponse.getShortenedUrl() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitMatrix matrix = new QRCodeWriter().encode(shortenUrlResponse.getShortenedUrl(), BarcodeFormat.QR_CODE, 300, 300);
                MatrixToImageWriter.writeToStream(matrix, "PNG", stream);

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(stream.toByteArray());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(shortenUrlResponse.getErrorMessage().getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Error generating short URL : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes(StandardCharsets.UTF_8));
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
