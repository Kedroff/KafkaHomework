package org.example.clientprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.clientProcessing.ClientDto;
import dto.clientProcessing.RegistrationRequest;
import dto.clientProcessing.UserDto;
import enums.clientProcessing.DocumentType;
import org.example.clientprocessing.service.ClientService;
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

@WebMvcTest(controllers = ClientController.class)
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClientService clientService;

    @Test
    void registerClient_201() throws Exception {
        RegistrationRequest req = new RegistrationRequest();
        req.setLogin("testuser");
        req.setPassword("password");
        req.setEmail("test@test.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req.setDocumentType(DocumentType.PASSPORT);
        req.setDocumentId("123456");

        UserDto userDto = new UserDto();
        userDto.setLogin("testuser");

        when(clientService.registerClient(any())).thenReturn(userDto);
        String body = mapper.writeValueAsString(req);

        mockMvc.perform(post("/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    void getClient_200() throws Exception {
        Long id = 1L;
        ClientDto dto = new ClientDto();
        dto.setClientId("770100000001");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(clientService.getClientById(id)).thenReturn(dto);

        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.clientId").value("770100000001"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getClient_404() throws Exception {
        Long id = 999L;
        when(clientService.getClientById(id)).thenReturn(null);

        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClients_200() throws Exception {
        ClientDto dto1 = new ClientDto();
        dto1.setClientId("770100000001");
        dto1.setFirstName("John");
        ClientDto dto2 = new ClientDto();
        dto2.setClientId("770100000002");
        dto2.setFirstName("Jane");

        when(clientService.getAllClients()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void registerClient_400_invalidData() throws Exception {
        RegistrationRequest req = new RegistrationRequest();
        req.setLogin("");
        req.setPassword("");
        req.setEmail("invalid-email");

        String body = mapper.writeValueAsString(req);

        mockMvc.perform(post("/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerClient_400_missingFields() throws Exception {
        String invalidJson = "{ \"login\": \"test\" }";

        mockMvc.perform(post("/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
