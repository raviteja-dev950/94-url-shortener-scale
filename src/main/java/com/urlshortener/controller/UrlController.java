package com.urlshortener.controller;

import com.urlshortener.entity.UrlMapping;
import com.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("project", "94-url-shortener-scale");
        response.put("status", "Live - URL Shortener Scale");
        response.put("tier", "10 - System Design Level - Scale + Polish");
        response.put("owner", "Raviteja");
        response.put("skills", "Caching, Security, Testing, Design Patterns");
        response.put("tools", "JUnit, Mockito, Redis (Caffeine), Elasticsearch");
        response.put("endpoints", new String[]{"/api/shorten", "/{shortCode}", "/stats/{shortCode}", "/health", "/h2-console"});
        response.put("message", "Try POST /api/shorten with {\"url\":\"https://google.com\"}");
        response.put("github", "https://github.com/raviteja-dev950/94-url-shortener-scale");
        response.put("render", "https://nine4-url-shortener-scale.onrender.com");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("project", "94-url-shortener-scale");
        response.put("status", "UP");
        response.put("tier", "10");
        response.put("cache", "Caffeine - Active");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");

        if (originalUrl == null || originalUrl.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "URL is required - Send {\"url\":\"https://google.com\"}");
            return ResponseEntity.badRequest().body(error);
        }

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        UrlMapping mapping = urlShortenerService.createShortUrl(originalUrl);
        String shortCode = mapping.getShortCode();
        String shortUrl = "https://nine4-url-shortener-scale.onrender.com/" + shortCode;

        Map<String, String> response = new HashMap<>();
        response.put("originalUrl", originalUrl);
        response.put("shortCode", shortCode);
        response.put("shortUrl", shortUrl);
        response.put("localUrl", "http://localhost:8096/" + shortCode);
        response.put("statsUrl", "http://localhost:8096/stats/" + shortCode);
        response.put("message", "URL shortened successfully - Scale ready - Base62 + Caffeine Cache");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        // Skip if it's stats or api
        if (shortCode.equals("stats") || shortCode.equals("api") || shortCode.equals("health") || shortCode.equals("h2-console")) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<UrlMapping> optional = urlShortenerService.getOriginalUrl(shortCode);

        if (optional.isPresent()) {
            UrlMapping mapping = optional.get();
            urlShortenerService.incrementClickCount(mapping);
            String originalUrl = mapping.getOriginalUrl();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<Map<String, Object>> stats(@PathVariable String shortCode) {
        Optional<UrlMapping> optional = urlShortenerService.getStats(shortCode);
        if (optional.isPresent()) {
            UrlMapping m = optional.get();
            Map<String, Object> resp = new HashMap<>();
            resp.put("shortCode", m.getShortCode());
            resp.put("originalUrl", m.getOriginalUrl());
            resp.put("clickCount", m.getClickCount());
            resp.put("createdAt", m.getCreatedAt() != null ? m.getCreatedAt().toString() : "N/A");
            resp.put("message", "Analytics - Click count increments on each redirect");
            return ResponseEntity.ok(resp);
        } else {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Short code not found");
            err.put("searchedCode", shortCode);
            err.put("hint", "H2 is in-memory - Codes deleted on restart - Create new with POST /api/shorten");
            err.put("example", "POST http://localhost:8096/api/shorten with {\"url\":\"https://google.com\"}");
            return ResponseEntity.status(404).body(err);
        }
    }
}
