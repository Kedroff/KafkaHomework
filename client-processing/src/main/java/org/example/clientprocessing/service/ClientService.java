package org.example.clientprocessing.service;

import dto.clientProcessing.ClientDto;
import dto.clientProcessing.RegistrationRequest;
import dto.clientProcessing.UserDto;
import java.util.List;

public interface ClientService {
    UserDto registerClient(RegistrationRequest request);
    ClientDto getClientById(Long id);
    List<ClientDto> getAllClients();
}
