package edu.eci.spti.demo.controller;

import edu.eci.spti.demo.domain.PhishingValidator;
import edu.eci.spti.demo.service.GoogleSafeBrowsingService;
import edu.eci.spti.demo.service.VirusTotalService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/phishing")
public class PhishingDetectionController {

    private final VirusTotalService virusTotalService;
    private final GoogleSafeBrowsingService googleSafeBrowsingService;

    public PhishingDetectionController(VirusTotalService virusTotalService, GoogleSafeBrowsingService googleSafeBrowsingService) {
        this.virusTotalService = virusTotalService;
        this.googleSafeBrowsingService = googleSafeBrowsingService;
    }

    @PostMapping("/analyze")
    public Mono<Map<String, Object>> analyzeContent(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        // Análisis heurístico
        if(request.containsKey("content")){
            String content = request.get("content");
            boolean containsPhishingKeywords = PhishingValidator.containsPhishingKeywords(content);
            response.put("heuristic_analysis", containsPhishingKeywords ? "Texto sospechoso" : "Texto normal");
        }

        // Escaneo de URL si existe
        if (request.containsKey("url")) {
            String url = request.get("url");
            boolean isSuspicious = PhishingValidator.isSuspiciousUrl(url);
            response.put("suspicious_url_pattern", isSuspicious);

            return Mono.zip(
                    virusTotalService.scanUrl(url),
                    googleSafeBrowsingService.checkUrl(url)
            ).map(results -> {
                response.put("virus_total", results.getT1());
                response.put("google_safe_browsing", results.getT2());
                return response;
            });
        }
        return Mono.just(response);
    }
}

