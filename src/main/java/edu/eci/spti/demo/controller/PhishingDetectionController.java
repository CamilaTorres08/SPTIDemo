package edu.eci.spti.demo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.spti.demo.service.VirusTotalService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/phishing")
@CrossOrigin("*")
public class PhishingDetectionController {

    private final VirusTotalService virusTotalService;

    public PhishingDetectionController(VirusTotalService virusTotalService) {
        this.virusTotalService = virusTotalService;
    }

    @PostMapping
    public Mono<Map<String, Object>> analyzeContent(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        return virusTotalService.analyzeUrl(url);
    }
}

