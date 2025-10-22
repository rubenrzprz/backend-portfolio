package com.ruben.backendportfolio;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelloServiceTest {

    @Test
    void greet_includes_fixed_timestamp() {
        String timestamp = "2025-01-01T00:00:00Z";
        Clock fixed = Clock.fixed(Instant.parse(timestamp), ZoneOffset.UTC);
        HelloService service = new HelloService(fixed);

        String result = service.greet();

        assertTrue(result.contains(timestamp),
                "greet() should include the fixed timestamp");
    }
}
