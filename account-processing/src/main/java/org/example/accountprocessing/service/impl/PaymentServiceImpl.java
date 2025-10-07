package org.example.accountprocessing.service.impl;

import dto.accountProcessing.PaymentDto;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        log.info("создание платежа: {}", dto);
        return dto;
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        log.info("получение платежа по id={}", id);
        return null;
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        log.info("получение всех платежей");
        return Collections.emptyList();
    }

    @Override
    public List<PaymentDto> getPaymentsByAccountId(Long accountId) {
        log.info("получение платежей по accountId={}", accountId);
        return Collections.emptyList();
    }
}
