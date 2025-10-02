package org.example.clientprocessing.service;

import dto.clientProcessing.ClientProductDto;
import java.util.List;

public interface ClientProductService {
    ClientProductDto createClientProduct(ClientProductDto dto);
    ClientProductDto getClientProductById(Long id);
    List<ClientProductDto> getClientProductsByClientId(Long clientId);
    ClientProductDto updateClientProduct(Long id, ClientProductDto dto);
    void deleteClientProduct(Long id);
}
