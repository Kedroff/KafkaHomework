package org.example.clientprocessing.controller;

import dto.clientProcessing.ClientDto;
import dto.clientProcessing.UserDto;
import dto.clientProcessing.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.example.clientprocessing.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerClient(@Valid @RequestBody RegistrationRequest request) {
        UserDto user = clientService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
}
