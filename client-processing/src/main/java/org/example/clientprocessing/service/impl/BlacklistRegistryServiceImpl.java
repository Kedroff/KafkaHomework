package org.example.clientprocessing.service.impl;

import dto.clientProcessing.BlacklistRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.example.clientprocessing.service.BlacklistRegistryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class BlacklistRegistryServiceImpl implements BlacklistRegistryService {

    @Override
    public BlacklistRegistryDto createBlacklistRegistry(BlacklistRegistryDto dto) {
        log.info("создание BlacklistRegistry: {}", dto);
        return dto;
    }

    @Override
    public BlacklistRegistryDto getBlacklistRegistryById(Long id) {
        log.info("получение BlacklistRegistry по id={}", id);
        return null;
    }

    @Override
    public List<BlacklistRegistryDto> getAllBlacklistRegistries() {
        log.info("получение всех BlacklistRegistry");
        return Collections.emptyList();
    }

    @Override
    public BlacklistRegistryDto updateBlacklistRegistry(Long id, BlacklistRegistryDto dto) {
        log.info("обновление BlacklistRegistry id={} данными {}", id, dto);
        return dto;
    }

    @Override
    public void deleteBlacklistRegistry(Long id) {
        log.info("удаление BlacklistRegistry id={}", id);
    }
}
