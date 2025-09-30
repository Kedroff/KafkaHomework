package org.example.clientprocessing.service.impl;

import dto.clientProcessing.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.clientprocessing.mapper.ProductMapper;
import org.example.clientprocessing.model.Product;
import org.example.clientprocessing.repository.ProductRepository;
import org.example.clientprocessing.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto dto) {
        log.info("Создать продукт: {}", dto);
        Product saved = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto getProductById(Long id) {
        log.info("Получить продукт по id: {}", id);
        return productRepository.findById(id).map(productMapper::toDto).orElse(null);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        log.info("Получить все продукты");
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        log.info("Обновить продукт id={}, данные={}", id, dto);
        return productRepository.findById(id)
                .map(existing -> productRepository.save(productMapper.partialUpdate(dto, existing)))
                .map(productMapper::toDto)
                .orElse(null);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Удалить продукт id={}", id);
        productRepository.deleteById(id);
    }
}
