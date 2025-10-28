package com.ruben.backendportfolio.items.service;

import com.ruben.backendportfolio.items.domain.Item;
import com.ruben.backendportfolio.items.domain.ItemNotFoundException;
import com.ruben.backendportfolio.items.dto.ItemCreateRequest;
import com.ruben.backendportfolio.items.dto.ItemUpdateRequest;
import com.ruben.backendportfolio.items.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemService {

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> list() {
        return repo.findAll();
    }

    public Item get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Item create(ItemCreateRequest req) {
        Item toSave = new Item(null, req.name());
        return repo.save(toSave);
    }

    public Item replace(Long id, ItemUpdateRequest req) {
        repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        Item updated = new Item(id, req.name());
        return repo.save(updated);
    }

    public Item patchName(Long id, ItemUpdateRequest req) {
        Item current = repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        String newName = (req.name() != null && !req.name().isBlank()) ? req.name() : current.name();
        Item updated = new Item(id, newName);
        return repo.save(updated);
    }

    public void delete(Long id) {
        if(!repo.existsById(id)) throw new ItemNotFoundException(id);
        repo.deleteById(id);
    }
}
