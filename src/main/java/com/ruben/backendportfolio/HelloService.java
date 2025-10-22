package com.ruben.backendportfolio;

import java.time.Clock;
import java.time.Instant;

public class HelloService {
    private final Clock clock;

    public HelloService(Clock clock) {
        this.clock = clock;
    }

    public String greet() {
        return "Hello, backend! " + Instant.now(clock);
    }
}
