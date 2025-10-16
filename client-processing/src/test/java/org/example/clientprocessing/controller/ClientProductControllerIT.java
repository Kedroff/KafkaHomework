package org.example.clientprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.clientProcessing.ClientProductDto;
import enums.clientProcessing.ClientProductStatus;
import org.example.clientprocessing.service.ClientProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientProductController.class)
class ClientProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClientProductService clientProductService;

    @Test
    void createClientProduct_201() throws Exception {
        ClientProductDto dto = new ClientProductDto();
        dto.setClientId(1L);
        dto.setProductId(1L);
        dto.setOpenDate(LocalDate.now());
        dto.setStatus(ClientProductStatus.ACTIVE);

        when(clientProductService.createClientProduct(any())).thenReturn(dto);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/client-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void getClientProduct_200() throws Exception {
        Long id = 1L;
        ClientProductDto dto = new ClientProductDto();
        dto.setClientId(1L);
        dto.setProductId(1L);

        when(clientProductService.getClientProductById(id)).thenReturn(dto);

        mockMvc.perform(get("/client-products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    void getClientProduct_404() throws Exception {
        Long id = 999L;
        when(clientProductService.getClientProductById(id)).thenReturn(null);

        mockMvc.perform(get("/client-products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getClientProductsByClientId_200() throws Exception {
        Long clientId = 1L;
        ClientProductDto dto1 = new ClientProductDto();
        dto1.setClientId(clientId);
        dto1.setProductId(1L);
        ClientProductDto dto2 = new ClientProductDto();
        dto2.setClientId(clientId);
        dto2.setProductId(2L);

        when(clientProductService.getClientProductsByClientId(clientId))
                .thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/client-products/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateClientProduct_200() throws Exception {
        Long id = 1L;
        ClientProductDto dto = new ClientProductDto();
        dto.setStatus(ClientProductStatus.CLOSED);

        when(clientProductService.updateClientProduct(id, dto)).thenReturn(dto);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(put("/client-products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void updateClientProduct_404() throws Exception {
        Long id = 999L;
        ClientProductDto dto = new ClientProductDto();
        when(clientProductService.updateClientProduct(id, dto)).thenReturn(null);
        String body = mapper.writeValueAsString(dto);

        mockMvc.perform(put("/client-products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClientProduct_204() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/client-products/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void createClientProduct_400_invalidData() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/client-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
