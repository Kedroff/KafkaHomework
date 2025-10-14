package org.example.creditprocessing.service;

import dto.creditProcessing.ProductRegistryDto;
import java.util.List;

public interface ProductRegistryService {
    ProductRegistryDto createProductRegistry(ProductRegistryDto dto);
    ProductRegistryDto getProductRegistryById(Long id);
    List<ProductRegistryDto> getAllProductRegistries();
    ProductRegistryDto updateProductRegistry(Long id, ProductRegistryDto dto);
    void deleteProductRegistry(Long id);
    
    void processClientCreditProduct(ProductRegistryDto dto);
}
