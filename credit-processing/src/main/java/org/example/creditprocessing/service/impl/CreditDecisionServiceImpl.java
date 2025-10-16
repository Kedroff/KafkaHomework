package org.example.creditprocessing.service.impl;

import dto.clientProcessing.ClientProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.creditprocessing.repository.ProductRegistryRepository;
import org.example.creditprocessing.service.CreditDecisionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditDecisionServiceImpl implements CreditDecisionService {

    private final ProductRegistryRepository productRegistryRepository;
    private final RestTemplate restTemplate;

    @Value("${t1.credit.limit}")
    private BigDecimal creditLimit;

    @Value("${integration.ms1.base-url}")
    private String ms1BaseUrl;

    @Value("${t1.credit.default-principal}")
    private BigDecimal defaultPrincipal;

    @Override
    public boolean isApproved(Long clientId, ClientProductDto desiredProduct) {
        try {
            restTemplate.getForObject(ms1BaseUrl + "/clients/" + clientId, Object.class);
        } catch (Exception ex) {
            log.warn("не удалось получить данные клиента в МС-1, id={}: {}", clientId, ex.getMessage());
        }

        long existingCount = productRegistryRepository.findAll().stream()
                .filter(pr -> clientId.equals(pr.getClientId()))
                .count();
        BigDecimal existing = defaultPrincipal.multiply(BigDecimal.valueOf(existingCount));
        BigDecimal desired = defaultPrincipal;
        boolean within = existing.add(desired).compareTo(creditLimit) <= 0;

        return within;
    }
}


