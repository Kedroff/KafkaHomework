package org.example.clientprocessing.service.impl;

import dto.clientProcessing.ClientDto;
import dto.clientProcessing.RegistrationRequest;
import dto.clientProcessing.UserDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.clientprocessing.mapper.ClientMapper;
import org.example.clientprocessing.mapper.UserMapper;
import org.example.clientprocessing.model.Client;
import org.example.clientprocessing.model.User;
import org.example.clientprocessing.repository.BlacklistRegistryRepository;
import org.example.clientprocessing.repository.ClientRepository;
import org.example.clientprocessing.repository.RoleRepository;
import org.example.clientprocessing.repository.UserRepository;
import org.example.clientprocessing.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final BlacklistRegistryRepository blacklistRepository;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserDto registerClient(RegistrationRequest req) {
        log.info("Регистрация клиента: {}", req);

        blacklistRepository.findByDocumentTypeAndDocumentId(
                req.getDocumentType(), req.getDocumentId()
        ).ifPresent(b -> { throw new IllegalStateException("Клиент в чёрном списке"); });

        User user = User.builder()
                .login(req.getLogin())
                .password(req.getPassword())
                .email(req.getEmail())
                .build();
        user = userRepository.save(user);

        var currentRole = roleRepository.findByName(enums.security.RoleName.ROLE_CURRENT_CLIENT).orElse(null);
        if (currentRole != null) {
            java.util.HashSet<org.example.clientprocessing.model.Role> set = new java.util.HashSet<>();
            set.add(currentRole);
            user.setRoles(set);
            userRepository.save(user);
        }

        long seq = ((Number) entityManager.createNativeQuery("SELECT nextval('client_number_seq')").getSingleResult()).longValue();
        String clientId = String.format("7701%08d", seq);

        Client client = Client.builder()
                .clientId(clientId)
                .userId(user.getId())
                .firstName(req.getFirstName())
                .middleName(req.getMiddleName())
                .lastName(req.getLastName())
                .dateOfBirth(req.getDateOfBirth())
                .documentType(req.getDocumentType())
                .documentId(req.getDocumentId())
                .documentPrefix(req.getDocumentPrefix())
                .documentSuffix(req.getDocumentSuffix())
                .build();
        clientRepository.save(client);

        return userMapper.toDto(user);
    }

    @Override
    public ClientDto getClientById(Long id) {
        log.info("Получить клиента по id: {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto).orElse(null);
    }

    @Override
    public List<ClientDto> getAllClients() {
        log.info("Получить всех клиентов");
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }
}
