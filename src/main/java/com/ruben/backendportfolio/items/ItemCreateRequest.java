package com.ruben.backendportfolio.items;

import com.ruben.backendportfolio.validation.ValidName;

public record ItemCreateRequest(@ValidName String name) {}
