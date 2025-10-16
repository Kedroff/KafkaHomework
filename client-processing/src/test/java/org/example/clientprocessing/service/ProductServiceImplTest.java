package org.example.clientprocessing.service;

import dto.clientProcessing.ProductDto;
import enums.clientProcessing.ProductKey;
import org.example.clientprocessing.mapper.ProductMapper;
import org.example.clientprocessing.model.Product;
import org.example.clientprocessing.repository.ProductRepository;
import org.example.clientprocessing.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private ProductServiceImpl productService;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        productMapper = mock(ProductMapper.class);
        productService = new ProductServiceImpl(productRepository, productMapper);
    }

    @Test
    void createProduct_success() {
        ProductDto dto = new ProductDto();
        dto.setName("Test Product");
        dto.setKey(ProductKey.DC);
        dto.setCreateDate(LocalDate.now());

        Product entity = Product.builder()
                .id(1L)
                .name("Test Product")
                .key(ProductKey.DC)
                .createDate(LocalDate.now())
                .build();

        when(productMapper.toEntity(dto)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(productMapper.toDto(entity)).thenReturn(dto);

        ProductDto result = productService.createProduct(dto);

        assertThat(result).isNotNull();
        verify(productRepository).save(entity);
    }

    @Test
    void getProductById_found() {
        Long id = 1L;
        Product entity = Product.builder().id(id).name("Test").build();
        ProductDto dto = new ProductDto();

        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDto(entity)).thenReturn(dto);

        ProductDto result = productService.getProductById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void getProductById_notFound() {
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductDto result = productService.getProductById(id);

        assertThat(result).isNull();
    }

    @Test
    void getAllProducts_success() {
        Product product1 = Product.builder().id(1L).name("Product 1").build();
        Product product2 = Product.builder().id(2L).name("Product 2").build();
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(new ProductDto());

        List<ProductDto> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
    }

    @Test
    void updateProduct_success() {
        Long id = 1L;
        ProductDto dto = new ProductDto();
        dto.setName("Updated Product");

        Product existing = Product.builder().id(id).name("Old Product").build();
        Product updated = Product.builder().id(id).name("Updated Product").build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productMapper.partialUpdate(dto, existing)).thenReturn(updated);
        when(productRepository.save(updated)).thenReturn(updated);
        when(productMapper.toDto(updated)).thenReturn(dto);

        ProductDto result = productService.updateProduct(id, dto);

        assertThat(result).isNotNull();
        verify(productRepository).save(updated);
    }

    @Test
    void deleteProduct_success() {
        Long id = 1L;
        productService.deleteProduct(id);
        verify(productRepository).deleteById(id);
    }
}
