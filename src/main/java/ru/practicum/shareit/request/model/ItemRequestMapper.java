package ru.practicum.shareit.request.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

@Service
public class ItemRequestMapper {

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(), userId, LocalDateTime.now());
    }

    public ItemRequestDto fromItemRequest(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }
}
