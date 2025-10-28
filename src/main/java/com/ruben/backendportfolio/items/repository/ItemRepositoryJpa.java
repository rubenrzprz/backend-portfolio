package com.ruben.backendportfolio.items.repository;

import com.ruben.backendportfolio.data.TaskEntity;
import com.ruben.backendportfolio.data.TaskJpaRepository;
import com.ruben.backendportfolio.items.domain.Item;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Primary // prefer this over any in-memory bean
public class ItemRepositoryJpa implements ItemRepository {

    private final TaskJpaRepository jpa;

    public ItemRepositoryJpa(TaskJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Item> findAll() {
        return jpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Item save(Item item) {
        TaskEntity e = (item.id() != null)
                ? jpa.findById(item.id()).orElseGet(TaskEntity::new)
                : new TaskEntity();

        if (e.getId() == null) {
            e.setUserId(1L);              // simple default owner for now
            e.setStatus("todo");
            e.setCreatedAt(OffsetDateTime.now());
        }
        e.setTitle(item.name());

        TaskEntity saved = jpa.save(e);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    private Item toDomain(TaskEntity te) {
        return new Item(te.getId(), te.getTitle());
    }
}
