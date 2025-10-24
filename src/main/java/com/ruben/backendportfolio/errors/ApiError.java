package com.ruben.backendportfolio.errors;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ApiError(
        String error,
        String message,
        List<Map<String, String>> details,
        String path,
        String timestamp
) {
    public static ApiError of(String code, String message, List<Map<String, String>> details, String path) {
        return new ApiError(code, message, details, path, Instant.now().toString());
    }

    public static ApiError of(String code, String message, String path) {
        return of(code, message, null, path);
    }
}
