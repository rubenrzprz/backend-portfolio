package com.ruben.backendportfolio.items;

public class ItemNotFoundException extends NotFoundException {
    private final Long id;

    public ItemNotFoundException(Long id) {
        super("Item %d not found".formatted(id));
        this.id = id;
    }

    public Long id() {
        return id;
    }
}
