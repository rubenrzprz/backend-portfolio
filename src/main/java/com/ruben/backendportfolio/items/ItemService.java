package com.ruben.backendportfolio.items;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemService {

    private final ItemRepository repo;
    private final AtomicLong idSeq = new AtomicLong(0L);

    public ItemService() {
        // Later we'll inject a JPA repository here instead
        this.repo = new InMemoryItemRepository();
    }

    public List<Item> list() {
        return repo.findAll();
    }

    public Item get(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new NotFoundException("Item %d not found".formatted(id)));
    }

    public Item create(ItemCreateRequest req) {
        long id = idSeq.incrementAndGet();
        Item item = new Item(id, req.name());
        return repo.save(item);
    }

    public Item replace(Long id, ItemUpdateRequest req) {
        if(!repo.existsById(id)) {
            throw new NotFoundException("Item %d not found".formatted(id));
        }
        Item updated = new Item(id, req.name());
        return repo.save(updated);
    }

    public Item patchName(Long id, ItemUpdateRequest req) {
        Item current = repo.findById(id).orElseThrow(() ->
                new NotFoundException("Item %d not found".formatted(id)));
        Item updated = new Item(id, req.name());
        return repo.save(updated);
    }

    public void delete(Long id) {
        if(!repo.existsById(id)) {
            throw new NotFoundException("Item %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}
