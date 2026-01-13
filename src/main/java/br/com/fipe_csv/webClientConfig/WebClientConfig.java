package br.com.fipe_csv.webClientConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://parallelum.com.br/fipe/api/v1")
                .build();
    }
}

