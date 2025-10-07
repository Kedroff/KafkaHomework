package org.example.creditprocessing.service.impl;

import dto.creditProcessing.ProductRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.example.creditprocessing.service.ProductRegistryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductRegistryServiceImpl implements ProductRegistryService {

    @Override
    public ProductRegistryDto createProductRegistry(ProductRegistryDto dto) {
        log.info("создание ProductRegistry: {}", dto);
        return dto;
    }

    @Override
    public ProductRegistryDto getProductRegistryById(Long id) {
        log.info("получение ProductRegistry по id={}", id);
        return null;
    }

    @Override
    public List<ProductRegistryDto> getAllProductRegistries() {
        log.info("получение всех ProductRegistry");
        return Collections.emptyList();
    }

    @Override
    public ProductRegistryDto updateProductRegistry(Long id, ProductRegistryDto dto) {
        log.info("обновление ProductRegistry id={} данными {}", id, dto);
        return dto;
    }

    @Override
    public void deleteProductRegistry(Long id) {
        log.info("удаление ProductRegistry id={}", id);
    }

    @Override
    public void processClientCreditProduct(ProductRegistryDto dto) {
        log.info("обработка сообщения из client_credit_products: {}", dto);
    }
}
