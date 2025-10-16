package org.example.clientprocessing.service;

import dto.clientProcessing.RegistrationRequest;
import dto.clientProcessing.UserDto;
import enums.clientProcessing.DocumentType;
import org.example.clientprocessing.mapper.ClientMapper;
import org.example.clientprocessing.mapper.UserMapper;
import org.example.clientprocessing.model.Client;
import org.example.clientprocessing.model.Role;
import org.example.clientprocessing.model.User;
import org.example.clientprocessing.repository.BlacklistRegistryRepository;
import org.example.clientprocessing.repository.ClientRepository;
import org.example.clientprocessing.repository.RoleRepository;
import org.example.clientprocessing.repository.UserRepository;
import org.example.clientprocessing.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private RoleRepository roleRepository;
    private BlacklistRegistryRepository blacklistRepository;
    private UserMapper userMapper;
    private ClientMapper clientMapper;
    private EntityManager entityManager;
    private ClientServiceImpl clientService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        clientRepository = mock(ClientRepository.class);
        roleRepository = mock(RoleRepository.class);
        blacklistRepository = mock(BlacklistRegistryRepository.class);
        userMapper = mock(UserMapper.class);
        clientMapper = mock(ClientMapper.class);
        entityManager = mock(EntityManager.class);
        
        clientService = new ClientServiceImpl(
                userRepository, clientRepository, roleRepository, 
                blacklistRepository, userMapper, clientMapper, entityManager
        );
    }

    @Test
    void registerClient_success() {
        RegistrationRequest req = new RegistrationRequest();
        req.setLogin("testuser");
        req.setPassword("password");
        req.setEmail("test@test.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req.setDocumentType(DocumentType.PASSPORT);
        req.setDocumentId("123456");

        when(blacklistRepository.findByDocumentTypeAndDocumentId(DocumentType.PASSPORT, "123456"))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(roleRepository.findByName(enums.security.RoleName.ROLE_CURRENT_CLIENT))
                .thenReturn(Optional.of(Role.builder().id(1L).name(enums.security.RoleName.ROLE_CURRENT_CLIENT).build()));

        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto());

        UserDto result = clientService.registerClient(req);

        assertThat(result).isNotNull();
        verify(userRepository, atLeastOnce()).save(any(User.class));
        verify(clientRepository, atLeastOnce()).save(any(Client.class));
    }

    @Test
    void registerClient_blacklisted_throwsException() {
        RegistrationRequest req = new RegistrationRequest();
        req.setDocumentType(DocumentType.PASSPORT);
        req.setDocumentId("123456");

        when(blacklistRepository.findByDocumentTypeAndDocumentId(DocumentType.PASSPORT, "123456"))
                .thenReturn(Optional.of(mock(org.example.clientprocessing.model.BlacklistRegistry.class)));

        assertThatThrownBy(() -> clientService.registerClient(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Клиент в чёрном списке");
    }

    @Test
    void getClientById_found() {
        Long id = 1L;
        Client client = Client.builder().id(id).clientId("770100000001").build();
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(new dto.clientProcessing.ClientDto());

        dto.clientProcessing.ClientDto result = clientService.getClientById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void getClientById_notFound() {
        Long id = 999L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        dto.clientProcessing.ClientDto result = clientService.getClientById(id);

        assertThat(result).isNull();
    }

    @Test
    void getAllClients_success() {
        Client client1 = Client.builder().id(1L).clientId("770100000001").build();
        Client client2 = Client.builder().id(2L).clientId("770100000002").build();
        when(clientRepository.findAll()).thenReturn(java.util.List.of(client1, client2));
        when(clientMapper.toDto(any(Client.class))).thenReturn(new dto.clientProcessing.ClientDto());

        java.util.List<dto.clientProcessing.ClientDto> result = clientService.getAllClients();

        assertThat(result).hasSize(2);
    }
}
