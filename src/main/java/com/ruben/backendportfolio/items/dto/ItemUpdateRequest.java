package com.ruben.backendportfolio.items.dto;

import com.ruben.backendportfolio.validation.ValidName;

public record ItemUpdateRequest(@ValidName String name) {}
