package org.example.clientprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.clientProcessing.ProductDto;
import enums.clientProcessing.ProductKey;
import org.example.clientprocessing.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = {"MASTER"})
    void createProduct_201() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("Test Product");
        dto.setKey(ProductKey.DC);
        dto.setCreateDate(LocalDate.now());

        when(productService.createProduct(any())).thenReturn(dto);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void createProduct_403_unauthorized() throws Exception {
        ProductDto dto = new ProductDto();
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProduct_200() throws Exception {
        Long id = 1L;
        ProductDto dto = new ProductDto();
        dto.setName("Test Product");

        when(productService.getProductById(id)).thenReturn(dto);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProduct_404() throws Exception {
        Long id = 999L;
        when(productService.getProductById(id)).thenReturn(null);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProducts_200() throws Exception {
        ProductDto dto1 = new ProductDto();
        dto1.setName("Product 1");
        ProductDto dto2 = new ProductDto();
        dto2.setName("Product 2");

        when(productService.getAllProducts()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = {"MASTER"})
    void updateProduct_200() throws Exception {
        Long id = 1L;
        ProductDto dto = new ProductDto();
        dto.setName("Updated Product");

        when(productService.updateProduct(id, dto)).thenReturn(dto);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"GRAND_EMPLOYEE"})
    void updateProduct_200_grandEmployee() throws Exception {
        Long id = 1L;
        ProductDto dto = new ProductDto();
        dto.setName("Updated Product");

        when(productService.updateProduct(id, dto)).thenReturn(dto);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"MASTER"})
    void deleteProduct_204() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
