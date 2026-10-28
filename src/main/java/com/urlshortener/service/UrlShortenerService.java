package com.urlshortener.service;

import com.urlshortener.entity.UrlMapping;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private final Random random = new Random();

    @Autowired
    private UrlRepository urlRepository;

    public String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int)(value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }

    public UrlMapping createShortUrl(String originalUrl) {
        String shortCode = generateShortCode();
        // Ensure unique
        while (urlRepository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(shortCode);
        mapping.setClickCount(0L);
        mapping.setCreatedAt(LocalDateTime.now());
        return urlRepository.save(mapping);
    }

    @Cacheable(value = "urlCache", key = "#shortCode")
    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    public void incrementClickCount(UrlMapping mapping) {
        mapping.setClickCount(mapping.getClickCount() + 1);
        urlRepository.save(mapping);
    }

    public Optional<UrlMapping> getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
}