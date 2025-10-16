package org.example.creditprocessing.service;

import dto.clientProcessing.ClientProductDto;
import org.example.creditprocessing.model.ProductRegistry;
import org.example.creditprocessing.repository.ProductRegistryRepository;
import org.example.creditprocessing.service.impl.CreditDecisionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreditDecisionServiceImplTest {

    private ProductRegistryRepository productRegistryRepository;
    private RestTemplate restTemplate;
    private CreditDecisionServiceImpl creditDecisionService;

    @BeforeEach
    void setup() {
        productRegistryRepository = mock(ProductRegistryRepository.class);
        restTemplate = mock(RestTemplate.class);
        creditDecisionService = new CreditDecisionServiceImpl(productRegistryRepository, restTemplate);
        
        ReflectionTestUtils.setField(creditDecisionService, "creditLimit", new BigDecimal("150000"));
        ReflectionTestUtils.setField(creditDecisionService, "defaultPrincipal", new BigDecimal("50000"));
        ReflectionTestUtils.setField(creditDecisionService, "ms1BaseUrl", "http://localhost:8080");
    }

    @Test
    void isApproved_true_whenNoExistingProducts() {
        Long clientId = 1L;
        ClientProductDto desiredProduct = new ClientProductDto();
        when(productRegistryRepository.findAll()).thenReturn(List.of());

        boolean result = creditDecisionService.isApproved(clientId, desiredProduct);

        assertThat(result).isTrue();
    }

    @Test
    void isApproved_true_whenWithinLimit() {
        Long clientId = 1L;
        ClientProductDto desiredProduct = new ClientProductDto();
        
        ProductRegistry existing1 = ProductRegistry.builder()
                .clientId(clientId)
                .build();
        ProductRegistry existing2 = ProductRegistry.builder()
                .clientId(clientId)
                .build();
        
        when(productRegistryRepository.findAll()).thenReturn(List.of(existing1, existing2));

        boolean result = creditDecisionService.isApproved(clientId, desiredProduct);

        assertThat(result).isTrue();
    }

    @Test
    void isApproved_false_whenExceedsLimit() {
        Long clientId = 1L;
        ClientProductDto desiredProduct = new ClientProductDto();
        
        ProductRegistry existing1 = ProductRegistry.builder()
                .clientId(clientId)
                .build();
        ProductRegistry existing2 = ProductRegistry.builder()
                .clientId(clientId)
                .build();
        ProductRegistry existing3 = ProductRegistry.builder()
                .clientId(clientId)
                .build();
        
        when(productRegistryRepository.findAll()).thenReturn(List.of(existing1, existing2, existing3));

        boolean result = creditDecisionService.isApproved(clientId, desiredProduct);

        assertThat(result).isFalse();
    }

    @Test
    void isApproved_ignoresOtherClients() {
        Long clientId = 1L;
        ClientProductDto desiredProduct = new ClientProductDto();
        
        ProductRegistry otherClient = ProductRegistry.builder()
                .clientId(999L)
                .build();
        
        when(productRegistryRepository.findAll()).thenReturn(List.of(otherClient));

        boolean result = creditDecisionService.isApproved(clientId, desiredProduct);

        assertThat(result).isTrue();
    }

    @Test
    void isApproved_handlesClientServiceError() {
        Long clientId = 1L;
        ClientProductDto desiredProduct = new ClientProductDto();
        
        when(productRegistryRepository.findAll()).thenReturn(List.of());
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        boolean result = creditDecisionService.isApproved(clientId, desiredProduct);

        assertThat(result).isTrue();
    }
}
