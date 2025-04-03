package edu.eci.spti.demo.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class VirusTotalService {

    private final WebClient webClient;
    private String apiKey = "585ec51f0adb7d85abbf1702bdd3ae52f33c5760a98f51b2ce224fa49ff49440";

    public VirusTotalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.virustotal.com/api/v3").build();
    }

    public Mono<Map<String, Object>> scanUrl(String url) {
        return webClient.post()
                .uri("/urls")
                .header("x-apikey", apiKey)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("url", url))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}




