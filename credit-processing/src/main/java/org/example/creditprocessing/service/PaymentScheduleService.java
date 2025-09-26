package org.example.creditprocessing.service;

import org.example.creditprocessing.model.PaymentRegistry;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentScheduleService {
    List<PaymentRegistry> generateAnnuitySchedule(BigDecimal principal, BigDecimal annualRate, int months);
}


