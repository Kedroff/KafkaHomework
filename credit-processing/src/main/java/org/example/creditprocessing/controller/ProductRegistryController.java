package org.example.creditprocessing.controller;

import dto.creditProcessing.ProductRegistryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.creditprocessing.service.ProductRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-registries")
public class ProductRegistryController {

    private final ProductRegistryService productRegistryService;

    @PostMapping
    public ResponseEntity<ProductRegistryDto> create(@Valid @RequestBody ProductRegistryDto dto) {
        return ResponseEntity.status(201).body(productRegistryService.createProductRegistry(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRegistryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productRegistryService.getProductRegistryById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductRegistryDto>> getAll() {
        return ResponseEntity.ok(productRegistryService.getAllProductRegistries());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductRegistryDto> update(@PathVariable Long id, @Valid @RequestBody ProductRegistryDto dto) {
        return ResponseEntity.ok(productRegistryService.updateProductRegistry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productRegistryService.deleteProductRegistry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> process(@Valid @RequestBody ProductRegistryDto dto) {
        productRegistryService.processClientCreditProduct(dto);
        return ResponseEntity.accepted().build();
    }
}
