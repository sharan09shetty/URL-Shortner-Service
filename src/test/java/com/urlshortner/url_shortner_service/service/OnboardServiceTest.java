package com.urlshortner.url_shortner_service.service;

import com.urlshortner.url_shortner_service.entity.ClientEntity;
import com.urlshortner.url_shortner_service.model.OnboardRequest;
import com.urlshortner.url_shortner_service.model.OnboardResponse;
import com.urlshortner.url_shortner_service.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class OnboardServiceTest {
    @InjectMocks
    private OnboardService onboardService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldOnboardClient(){
        OnboardRequest onboardRequest = OnboardRequest.builder()
                .clientName("Test")
                .clientIdentifier("unit-test")
                .build();

        when(clientRepository.findByClientIdentifier(anyString())).thenReturn(null);
        OnboardResponse onboardResponse = onboardService.onboardClient(onboardRequest);
        assertEquals(onboardRequest.getClientIdentifier(),onboardResponse.getClientIdentifier());
        assertNull(onboardResponse.getErrorMessage());
    }

    @Test
    public void shouldNotOnboardClientDueToExistingClientIdentifier(){
        OnboardRequest onboardRequest = OnboardRequest.builder()
                .clientName("Test")
                .clientIdentifier("unit-test")
                .build();
        ClientEntity Client = mock(ClientEntity.class);

        when(clientRepository.findByClientIdentifier(anyString())).thenReturn(Client);
        OnboardResponse onboardResponse = onboardService.onboardClient(onboardRequest);
        assertNull(onboardResponse.getClientIdentifier());
        assertNull(onboardResponse.getApiKey());
        assertEquals("Try with a different client-identifier.",onboardResponse.getErrorMessage());
    }
}
