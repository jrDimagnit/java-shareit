package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemMapper {
    public Item toItem(ItemDto itemDto, Long ownerId) {
        Item createItem = new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), ownerId);
        return createItem;
    }

    public ItemDto fromItem(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
