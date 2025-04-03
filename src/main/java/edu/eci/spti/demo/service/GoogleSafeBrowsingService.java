package edu.eci.spti.demo.service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GoogleSafeBrowsingService {

    private static final String API_KEY = "AIzaSyD9mMTsOMw1Apo1wPFm7B_oPTH5eC5_VBY";
    private final WebClient webClient;

    public GoogleSafeBrowsingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + API_KEY).build();
    }

    public Mono<Map<String, Object>> checkUrl(String url) {
        String requestBody = "{ \"client\": { \"clientId\": \"phishing-detector\", \"clientVersion\": \"1.0\" }, \"threatInfo\": { \"threatTypes\": [\"MALWARE\", \"SOCIAL_ENGINEERING\"], \"platformTypes\": [\"ANY_PLATFORM\"], \"threatEntryTypes\": [\"URL\"], \"threatEntries\": [{ \"url\": \"" + url + "\" }] } }";

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}


