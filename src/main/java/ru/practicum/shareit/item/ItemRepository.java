package ru.practicum.shareit.item;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class ItemRepository {
    private Long id = 0L;
    private HashMap<Long, Item> base = new HashMap<>();


    public Item getById(Long id) {
        return base.get(id);
    }

    public Item createItem(Item item) {
        item.setId(++id);
        base.put(id, item);
        return item;
    }

    public Item updateItem(ItemDto itemDto) {
        Item item = getById(itemDto.getId());
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    public List<Item> search(String text) {
        return base.values().stream().filter(item -> (item.getName().toLowerCase().trim()
                        .contains(text.toLowerCase()) || item.getDescription().toLowerCase().trim()
                        .contains(text.toLowerCase())) && item.getAvailable())
                .collect(Collectors.toList());
    }

}
