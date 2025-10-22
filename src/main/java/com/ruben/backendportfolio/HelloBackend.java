package com.ruben.backendportfolio;

import java.time.Clock;

public class HelloBackend {
    public static void main(String[] args) {
        HelloService service = new HelloService(Clock.systemUTC());
        System.out.println(service.greet());
    }
}