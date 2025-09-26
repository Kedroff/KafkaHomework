package org.example.creditprocessing.service;

import dto.creditProcessing.PaymentRegistryDto;
import java.util.List;

public interface PaymentRegistryService {
    PaymentRegistryDto createPaymentRegistry(PaymentRegistryDto dto);
    PaymentRegistryDto getPaymentRegistryById(Long id);
    List<PaymentRegistryDto> getAllPaymentRegistries();
    PaymentRegistryDto updatePaymentRegistry(Long id, PaymentRegistryDto dto);
    void deletePaymentRegistry(Long id);
}
