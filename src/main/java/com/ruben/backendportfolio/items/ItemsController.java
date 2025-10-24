package com.ruben.backendportfolio.items;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemsController {

    private final ItemService service;

    public ItemsController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Item>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable Long id) {
        Item item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Item> create(@Valid @RequestBody ItemCreateRequest req) {
        Item created = service.create(req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> replace(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest req
    ) {
        Item updated = service.replace(id, req);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> patchName(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest req
    ) {
        Item updated = service.patchName(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
