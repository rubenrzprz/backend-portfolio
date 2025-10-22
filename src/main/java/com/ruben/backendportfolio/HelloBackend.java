package com.ruben.backendportfolio;

public class HelloBackend {
    public static void main(String[] args) {
        HelloService service = new HelloService();
        System.out.println(service.greet());
    }
}