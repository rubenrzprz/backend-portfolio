package com.ruben.backendportfolio.items.dto;

import com.ruben.backendportfolio.validation.ValidName;

public record ItemCreateRequest(@ValidName String name) {}
