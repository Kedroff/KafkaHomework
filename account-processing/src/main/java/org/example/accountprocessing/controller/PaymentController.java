package org.example.accountprocessing.controller;

import dto.accountProcessing.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.example.accountprocessing.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(paymentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(paymentService.getPaymentsByAccountId(accountId));
    }
}
