package org.example.clientprocessing.controller;

import dto.clientProcessing.BlacklistRegistryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.clientprocessing.service.BlacklistRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklist-registries")
public class BlacklistRegistryController {

    private final BlacklistRegistryService blacklistRegistryService;

    @PostMapping
    public ResponseEntity<BlacklistRegistryDto> create(@Valid @RequestBody BlacklistRegistryDto dto) {
        return ResponseEntity.status(201).body(blacklistRegistryService.createBlacklistRegistry(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlacklistRegistryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blacklistRegistryService.getBlacklistRegistryById(id));
    }

    @GetMapping
    public ResponseEntity<List<BlacklistRegistryDto>> getAll() {
        return ResponseEntity.ok(blacklistRegistryService.getAllBlacklistRegistries());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlacklistRegistryDto> update(@PathVariable Long id,
                                                       @Valid @RequestBody BlacklistRegistryDto dto) {
        return ResponseEntity.ok(blacklistRegistryService.updateBlacklistRegistry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blacklistRegistryService.deleteBlacklistRegistry(id);
        return ResponseEntity.noContent().build();
    }
}
