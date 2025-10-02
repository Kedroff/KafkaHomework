package org.example.clientprocessing.service;

import dto.clientProcessing.BlacklistRegistryDto;

import java.util.List;

public interface BlacklistRegistryService {
    BlacklistRegistryDto createBlacklistRegistry(BlacklistRegistryDto dto);
    BlacklistRegistryDto getBlacklistRegistryById(Long id);
    List<BlacklistRegistryDto> getAllBlacklistRegistries();
    BlacklistRegistryDto updateBlacklistRegistry(Long id, BlacklistRegistryDto dto);
    void deleteBlacklistRegistry(Long id);
}
