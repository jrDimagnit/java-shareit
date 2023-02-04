package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;

import java.util.List;

@Service
public class ItemMapper {
    public Item toItem(ItemDto itemDto, Long ownerId, Long requestId) {
        Item createItem = new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), ownerId, requestId);
        return createItem;
    }

    public ItemDto fromItem(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequestId());
    }

    public ItemWithCommentsDto fromItemWithComments(Item item, List<CommentResponseDto> comments,
                                                    LastNextBookingDto lastBooking, LastNextBookingDto nextBooking) {
        return new ItemWithCommentsDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(), lastBooking, nextBooking, comments);
    }
}
