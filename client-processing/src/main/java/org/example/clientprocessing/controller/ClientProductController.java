package org.example.clientprocessing.controller;

import dto.clientProcessing.ClientProductDto;
import lombok.RequiredArgsConstructor;
import org.example.clientprocessing.service.ClientProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/client-products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService clientProductService;

    @PostMapping
    public ResponseEntity<ClientProductDto> createClientProduct(@Valid @RequestBody ClientProductDto clientProductDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientProductService.createClientProduct(clientProductDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientProductDto> getClientProduct(@PathVariable Long id) {
        return ResponseEntity.ok(clientProductService.getClientProductById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientProductDto>> getClientProducts(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientProductService.getClientProductsByClientId(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientProductDto> updateClientProduct(@PathVariable Long id,
                                                                @Valid @RequestBody ClientProductDto clientProductDto) {
        return ResponseEntity.ok(clientProductService.updateClientProduct(id, clientProductDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientProduct(@PathVariable Long id) {
        clientProductService.deleteClientProduct(id);
        return ResponseEntity.noContent().build();
    }
}
