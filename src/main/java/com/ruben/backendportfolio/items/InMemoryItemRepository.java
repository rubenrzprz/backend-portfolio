package com.ruben.backendportfolio.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> store = new ConcurrentHashMap<>();

    @Override
    public List<Item> findAll() {
        return  new ArrayList<>(store.values());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Item save(Item item) {
        store.put(item.id(), item);
        return item;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
