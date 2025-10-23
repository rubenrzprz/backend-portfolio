package com.ruben.backendportfolio.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ItemsController {

    @GetMapping("/items")
    public List<Map<String, Object>> items() {
        return List.of(
                Map.of("id", 1, "name", "alpha"),
                Map.of("id", 2, "name", "beta")
        );
    }
}
