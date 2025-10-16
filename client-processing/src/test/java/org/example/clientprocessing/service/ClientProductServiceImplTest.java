package org.example.clientprocessing.service;

import dto.clientProcessing.ClientProductDto;
import enums.clientProcessing.ClientProductStatus;
import enums.clientProcessing.ProductKey;
import org.example.clientprocessing.kafka.KafkaClientProducer;
import org.example.clientprocessing.mapper.ClientProductMapper;
import org.example.clientprocessing.model.ClientProduct;
import org.example.clientprocessing.model.Product;
import org.example.clientprocessing.repository.ClientProductRepository;
import org.example.clientprocessing.repository.ProductRepository;
import org.example.clientprocessing.service.impl.ClientProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClientProductServiceImplTest {

    private ClientProductRepository clientProductRepository;
    private ProductRepository productRepository;
    private ClientProductMapper clientProductMapper;
    private KafkaClientProducer kafkaClientProducer;
    private ClientProductServiceImpl clientProductService;

    @BeforeEach
    void setup() {
        clientProductRepository = mock(ClientProductRepository.class);
        productRepository = mock(ProductRepository.class);
        clientProductMapper = mock(ClientProductMapper.class);
        kafkaClientProducer = mock(KafkaClientProducer.class);
        
        clientProductService = new ClientProductServiceImpl(
                clientProductRepository, productRepository, clientProductMapper, kafkaClientProducer
        );
        ReflectionTestUtils.setField(clientProductService, "topicClientProducts", "client_products");
        ReflectionTestUtils.setField(clientProductService, "topicClientCreditProducts", "client_credit_products");
    }

    @Test
    void createClientProduct_sendsToClientProducts_forDC() {
        ClientProductDto dto = new ClientProductDto();
        dto.setProductId(1L);
        dto.setClientId(1L);
        dto.setOpenDate(LocalDate.now());
        dto.setStatus(ClientProductStatus.ACTIVE);

        ClientProduct entity = ClientProduct.builder()
                .id(1L)
                .clientId(1L)
                .productId(1L)
                .openDate(LocalDate.now())
                .status(ClientProductStatus.ACTIVE)
                .build();

        Product product = Product.builder()
                .id(1L)
                .key(ProductKey.DC)
                .name("Debit Card")
                .build();

        when(clientProductMapper.toEntity(dto)).thenReturn(entity);
        when(clientProductRepository.save(entity)).thenReturn(entity);
        when(clientProductMapper.toDto(entity)).thenReturn(dto);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ClientProductDto result = clientProductService.createClientProduct(dto);

        assertThat(result).isNotNull();
        verify(kafkaClientProducer).sendTo("client_products", dto);
    }

    @Test
    void createClientProduct_sendsToCreditProducts_forPC() {
        ClientProductDto dto = new ClientProductDto();
        dto.setProductId(2L);
        dto.setClientId(1L);
        dto.setOpenDate(LocalDate.now());
        dto.setStatus(ClientProductStatus.ACTIVE);

        ClientProduct entity = ClientProduct.builder()
                .id(1L)
                .clientId(1L)
                .productId(2L)
                .openDate(LocalDate.now())
                .status(ClientProductStatus.ACTIVE)
                .build();

        Product product = Product.builder()
                .id(2L)
                .key(ProductKey.PC)
                .name("Personal Credit")
                .build();

        when(clientProductMapper.toEntity(dto)).thenReturn(entity);
        when(clientProductRepository.save(entity)).thenReturn(entity);
        when(clientProductMapper.toDto(entity)).thenReturn(dto);
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        ClientProductDto result = clientProductService.createClientProduct(dto);

        assertThat(result).isNotNull();
        verify(kafkaClientProducer).sendTo("client_credit_products", dto);
    }

    @Test
    void getClientProductById_found() {
        Long id = 1L;
        ClientProduct entity = ClientProduct.builder().id(id).build();
        ClientProductDto dto = new ClientProductDto();

        when(clientProductRepository.findById(id)).thenReturn(Optional.of(entity));
        when(clientProductMapper.toDto(entity)).thenReturn(dto);

        ClientProductDto result = clientProductService.getClientProductById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void getClientProductById_notFound() {
        Long id = 999L;
        when(clientProductRepository.findById(id)).thenReturn(Optional.empty());

        ClientProductDto result = clientProductService.getClientProductById(id);

        assertThat(result).isNull();
    }

    @Test
    void getClientProductsByClientId_success() {
        Long clientId = 1L;
        ClientProduct cp1 = ClientProduct.builder().id(1L).clientId(clientId).build();
        ClientProduct cp2 = ClientProduct.builder().id(2L).clientId(clientId).build();
        ClientProduct cp3 = ClientProduct.builder().id(3L).clientId(2L).build();

        when(clientProductRepository.findAll()).thenReturn(List.of(cp1, cp2, cp3));
        when(clientProductMapper.toDto(any(ClientProduct.class))).thenReturn(new ClientProductDto());

        List<ClientProductDto> result = clientProductService.getClientProductsByClientId(clientId);

        assertThat(result).hasSize(2);
    }

    @Test
    void updateClientProduct_success() {
        Long id = 1L;
        ClientProductDto dto = new ClientProductDto();
        dto.setStatus(ClientProductStatus.CLOSED);

        ClientProduct existing = ClientProduct.builder().id(id).status(ClientProductStatus.ACTIVE).build();
        ClientProduct updated = ClientProduct.builder().id(id).status(ClientProductStatus.CLOSED).build();

        when(clientProductRepository.findById(id)).thenReturn(Optional.of(existing));
        when(clientProductMapper.partialUpdate(dto, existing)).thenReturn(updated);
        when(clientProductRepository.save(updated)).thenReturn(updated);
        when(clientProductMapper.toDto(updated)).thenReturn(dto);

        ClientProductDto result = clientProductService.updateClientProduct(id, dto);

        assertThat(result).isNotNull();
        verify(clientProductRepository).save(updated);
    }

    @Test
    void updateClientProduct_notFound() {
        Long id = 999L;
        ClientProductDto dto = new ClientProductDto();
        when(clientProductRepository.findById(id)).thenReturn(Optional.empty());

        ClientProductDto result = clientProductService.updateClientProduct(id, dto);

        assertThat(result).isNull();
    }

    @Test
    void deleteClientProduct_success() {
        Long id = 1L;
        clientProductService.deleteClientProduct(id);
        verify(clientProductRepository).deleteById(id);
    }
}
