package edu.eci.spti.demo.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@Service
public class VirusTotalService {

    private final WebClient webClient;
    private static final String API_KEY = "585ec51f0adb7d85abbf1702bdd3ae52f33c5760a98f51b2ce224fa49ff49440";

    public VirusTotalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.virustotal.com/api/v3").build();
    }

    public Mono<Map<String, Object>> analyzeUrl(String url) {
        return webClient.post()
                .uri("/urls")
                .header("x-apikey", API_KEY)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("url", url))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(response -> {
                    String analysisId = response.path("data").path("id").asText();
                    return fetchAnalysisResult(analysisId);
                });
    }

    private Mono<Map<String, Object>> fetchAnalysisResult(String analysisId) {
        return webClient.get()
                .uri("/analyses/" + analysisId)
                .header("x-apikey", API_KEY)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    JsonNode stats = response.path("data").path("attributes").path("stats");
                    int malicious = stats.path("malicious").asInt();
                    boolean isPhishing = malicious > 5;

                    return Map.of(
                            "virus_total", response,
                            "phishing", isPhishing
                    );
                });
    }
}




