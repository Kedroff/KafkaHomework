package org.example.accountprocessing.service;

import dto.accountProcessing.PaymentDto;
import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto dto);
    PaymentDto getPaymentById(Long id);
    List<PaymentDto> getAllPayments();
    List<PaymentDto> getPaymentsByAccountId(Long accountId);
}
