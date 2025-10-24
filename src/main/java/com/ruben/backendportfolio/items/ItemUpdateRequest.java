package com.ruben.backendportfolio.items;

import com.ruben.backendportfolio.validation.ValidName;

public record ItemUpdateRequest(@ValidName String name) {}
