package com.urlshortener.service;

import com.urlshortener.entity.UrlMapping;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UrlShortenerServiceTest {

    private final UrlShortenerService service = new UrlShortenerService();

    @Test
    public void testBase62Encode() {
        // Test Base62 encoding - Tier 10 Scale concept
        String encoded = service.encodeBase62(62);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        System.out.println("Base62 Encode Test Passed: 62 -> " + encoded);
    }

    @Test
    public void testShortCodeGeneration() {
        // Test short code generation - Should be 6 chars, alphanumeric
        String code = service.generateShortCode();
        assertNotNull(code);
        assertEquals(6, code.length());
        assertTrue(code.matches("[a-zA-Z0-9]+"));
        System.out.println("Short Code Test Passed: " + code);
    }
}