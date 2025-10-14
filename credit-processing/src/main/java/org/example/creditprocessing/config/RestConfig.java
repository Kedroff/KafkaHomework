package org.example.creditprocessing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import java.util.List;
import org.example.creditprocessing.security.ServiceJwt;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, ServiceJwt serviceJwt) {
        ClientHttpRequestInterceptor jwtInterceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + serviceJwt.issue());
            return execution.execute(request, body);
        };
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(List.of(jwtInterceptor))
                .build();
    }
}


