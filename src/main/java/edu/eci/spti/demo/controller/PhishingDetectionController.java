package edu.eci.spti.demo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.spti.demo.service.TextAnalyzerService;
import edu.eci.spti.demo.service.VirusTotalService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/phishing")
@CrossOrigin("*")
public class PhishingDetectionController {

    private final VirusTotalService virusTotalService;
    private final TextAnalyzerService textAnalyzer;

    public PhishingDetectionController(VirusTotalService virusTotalService, TextAnalyzerService textAnalyzer) {
        this.virusTotalService = virusTotalService;
        this.textAnalyzer = textAnalyzer;
    }

    @PostMapping
    public Mono<Map<String, Object>> analyzeContent(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        return virusTotalService.analyzeUrl(url);
    }

    @PostMapping("/text")
    public Mono<Map<String, Object>> analyzeText(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String url = textAnalyzer.extractUrl(text);
        return virusTotalService.analyzeUrl(url);
        
        
    }
}

