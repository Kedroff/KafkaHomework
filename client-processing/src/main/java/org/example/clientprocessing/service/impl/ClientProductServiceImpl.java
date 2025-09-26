package org.example.clientprocessing.service.impl;

import dto.clientProcessing.ClientProductDto;
import enums.clientProcessing.ProductKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.clientprocessing.kafka.KafkaClientProducer;
import org.example.clientprocessing.mapper.ClientProductMapper;
import org.example.clientprocessing.model.ClientProduct;
import org.example.clientprocessing.repository.ClientProductRepository;
import org.example.clientprocessing.repository.ProductRepository;
import org.example.clientprocessing.service.ClientProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private static final EnumSet<ProductKey> DEPOSIT_AND_CARD_KEYS = EnumSet.of(
            ProductKey.DC, ProductKey.CC, ProductKey.NS, ProductKey.PENS
    );

    private final ClientProductRepository clientProductRepository;
    private final ProductRepository productRepository;
    private final ClientProductMapper clientProductMapper;
    private final KafkaClientProducer kafkaClientProducer;

    @Value("${t1.kafka.topic.client_products}")
    private String topicClientProducts;
    @Value("${t1.kafka.topic.client_credit_products}")
    private String topicClientCreditProducts;

    @Override
    @Transactional
    public ClientProductDto createClientProduct(ClientProductDto dto) {
        log.info("Создать продукт клиента: {}", dto);

        ClientProduct saved = clientProductRepository.save(clientProductMapper.toEntity(dto));
        ClientProductDto result = clientProductMapper.toDto(saved);

        productRepository.findById(dto.getProductId()).ifPresent(product -> {
            ProductKey key = product.getKey();
            String topic = DEPOSIT_AND_CARD_KEYS.contains(key) ? topicClientProducts : topicClientCreditProducts;
            kafkaClientProducer.sendTo(topic, result);
        });

        return result;
    }

    @Override
    public ClientProductDto getClientProductById(Long id) {
        log.info("Получить продукт клиента по id: {}", id);
        return clientProductRepository.findById(id)
                .map(clientProductMapper::toDto)
                .orElse(null);
    }

    @Override
    public List<ClientProductDto> getClientProductsByClientId(Long clientId) {
        log.info("Получить продукты клиента по clientId: {}", clientId);

        return clientProductRepository.findAll().stream()
                .filter(cp -> clientId.equals(cp.getClientId()))
                .map(clientProductMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ClientProductDto updateClientProduct(Long id, ClientProductDto dto) {
        log.info("Обновить продукт клиента id={}, данные={}", id, dto);
        Optional<ClientProduct> existing = clientProductRepository.findById(id);
        if (existing.isEmpty()) return null;
        ClientProduct updated = clientProductMapper.partialUpdate(dto, existing.get());
        return clientProductMapper.toDto(clientProductRepository.save(updated));
    }

    @Override
    @Transactional
    public void deleteClientProduct(Long id) {
        log.info("Удалить продукт клиента id={}", id);
        clientProductRepository.deleteById(id);
    }
}
