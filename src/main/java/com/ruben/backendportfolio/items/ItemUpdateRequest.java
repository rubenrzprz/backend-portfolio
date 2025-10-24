package com.ruben.backendportfolio.items;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemUpdateRequest(
        @NotBlank(message = "name is required")
        @Size(min = 2, max = 40, message = "name must be 2-40 chars")
        String name
) {}
