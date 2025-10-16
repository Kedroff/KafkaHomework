package org.example.creditprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.creditprocessing.model.PaymentRegistry;
import org.example.creditprocessing.service.PaymentScheduleService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    @Override
    public List<PaymentRegistry> generateAnnuitySchedule(BigDecimal principal, BigDecimal annualRate, int months) {
        List<PaymentRegistry> list = new ArrayList<>();
        if (principal == null || annualRate == null || months <= 0) return list;

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), mc).divide(BigDecimal.valueOf(100), mc);
        BigDecimal onePlusRPowerN = (BigDecimal.ONE.add(monthlyRate)).pow(months, mc);
        BigDecimal annuity = principal.multiply(monthlyRate, mc).multiply(onePlusRPowerN, mc)
                .divide(onePlusRPowerN.subtract(BigDecimal.ONE, mc), mc);

        BigDecimal remaining = principal;
        LocalDate date = LocalDate.now();
        for (int m = 1; m <= months; m++) {
            BigDecimal interest = remaining.multiply(monthlyRate, mc);
            BigDecimal principalPart = annuity.subtract(interest, mc);
            remaining = remaining.subtract(principalPart, mc);

            PaymentRegistry pr = PaymentRegistry.builder()
                    .paymentDate(date.plusMonths(m))
                    .amount(annuity.setScale(2, RoundingMode.HALF_UP))
                    .interestRateAmount(interest.setScale(2, RoundingMode.HALF_UP))
                    .debtAmount(remaining.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP))
                    .expired(false)
                    .paymentExpirationDate(date.plusMonths(m))
                    .build();
            list.add(pr);
        }
        return list;
    }
}


