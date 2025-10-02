package org.example.creditprocessing.service.impl;

import dto.creditProcessing.PaymentRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.example.creditprocessing.service.PaymentRegistryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PaymentRegistryServiceImpl implements PaymentRegistryService {

    @Override
    public PaymentRegistryDto createPaymentRegistry(PaymentRegistryDto dto) {
        log.info("создание PaymentRegistry: {}", dto);
        return dto;
    }

    @Override
    public PaymentRegistryDto getPaymentRegistryById(Long id) {
        log.info("получение PaymentRegistry по id={}", id);
        return null;
    }

    @Override
    public List<PaymentRegistryDto> getAllPaymentRegistries() {
        log.info("получение всех PaymentRegistry");
        return Collections.emptyList();
    }

    @Override
    public PaymentRegistryDto updatePaymentRegistry(Long id, PaymentRegistryDto dto) {
        log.info("обновление PaymentRegistry id={} данными {}", id, dto);
        return dto;
    }

    @Override
    public void deletePaymentRegistry(Long id) {
        log.info("удаление PaymentRegistry id={}", id);
    }
}
