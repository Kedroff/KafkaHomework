package org.example.creditprocessing.controller;

import dto.creditProcessing.PaymentRegistryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.creditprocessing.service.PaymentRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-registries")
public class PaymentRegistryController {

    private final PaymentRegistryService paymentRegistryService;

    @PostMapping
    public ResponseEntity<PaymentRegistryDto> create(@Valid @RequestBody PaymentRegistryDto dto) {
        return ResponseEntity.status(201).body(paymentRegistryService.createPaymentRegistry(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentRegistryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentRegistryService.getPaymentRegistryById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentRegistryDto>> getAll() {
        return ResponseEntity.ok(paymentRegistryService.getAllPaymentRegistries());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentRegistryDto> update(@PathVariable Long id, @Valid @RequestBody PaymentRegistryDto dto) {
        return ResponseEntity.ok(paymentRegistryService.updatePaymentRegistry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentRegistryService.deletePaymentRegistry(id);
        return ResponseEntity.noContent().build();
    }
}
