package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private int id;

    public Item add(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    public Item getById(int id) {
        return items.get(id);
    }

    public Collection<Item> getAllToOwner(int owner) {
        return items.values().stream()
                .filter(item -> item.getOwner() == owner)
                .collect(Collectors.toList());
    }

    public Collection<Item> search(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }
}
