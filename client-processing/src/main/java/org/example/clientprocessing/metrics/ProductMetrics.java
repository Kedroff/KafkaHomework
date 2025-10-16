package org.example.clientprocessing.metrics;

import enums.clientProcessing.ClientProductStatus;
import enums.clientProcessing.ProductKey;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.example.clientprocessing.model.ClientProduct;
import org.example.clientprocessing.repository.ClientProductRepository;
import org.example.clientprocessing.repository.ProductRepository;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@RequiredArgsConstructor
public class ProductMetrics {

    private final ClientProductRepository clientProductRepository;
    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    private AtomicInteger dcCount = new AtomicInteger(0);
    private AtomicInteger ccCount = new AtomicInteger(0);
    private AtomicInteger acCount = new AtomicInteger(0);

    @PostConstruct
    void init() {
        Gauge.builder("open_products_dc", dcCount, AtomicInteger::get)
                .register(meterRegistry);
        Gauge.builder("open_products_cc", ccCount, AtomicInteger::get)
                .register(meterRegistry);
        Gauge.builder("open_products_ac", acCount, AtomicInteger::get)
                .register(meterRegistry);
        refresh();
    }

    public void refresh() {
        List<ClientProduct> all = clientProductRepository.findAll();
        Map<Long, ProductKey> productKeys = productRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(p -> p.getId(), p -> p.getKey()));

        int dc = 0;
        int cc = 0;
        int ac = 0;

        for (ClientProduct cp : all) {
            if (cp.getStatus() == ClientProductStatus.ACTIVE) {
                ProductKey key = productKeys.get(cp.getProductId());
                if (key == ProductKey.DC) dc++;
                if (key == ProductKey.CC) cc++;
                if (key == ProductKey.AC) ac++;
            }
        }

        dcCount.set(dc);
        ccCount.set(cc);
        acCount.set(ac);
    }
}
