package com.ruben.backendportfolio;

import java.time.Instant;

public class HelloService {
    public String greet() {
        return "Hello, backend! " + Instant.now();
    }
}
