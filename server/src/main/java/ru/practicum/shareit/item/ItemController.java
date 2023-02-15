package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final String owner = "X-Sharer-User-ID";
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(owner) Long userId,
                              @RequestBody ItemDto itemDto) {
        log.debug("Создание предмета {} от ползователя с id {}", itemDto, userId);
        return itemService.createItem(itemDto, userId, itemDto.getRequestId());
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(owner) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        log.debug("Запрос на изменения данных предмета с id {} от пользователя с id {}", itemDto.getId(), userId);
        return itemService.updateItem(itemDto, userId);
    }

    @GetMapping
    public Collection<ItemWithCommentsDto> getAllItemByUserId(@RequestHeader(owner) Long userId,
                                                              @RequestParam(defaultValue = "0") Integer from,
                                                              @RequestParam(defaultValue = "20") Integer size) {
        return itemService.getAllItemByUserId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsDto getItemById(@RequestHeader(owner) Long userId,
                                           @PathVariable Long itemId) {
        log.debug("Запрос предмета с id {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return text.isBlank() ? Collections.emptyList() : itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComments(@RequestHeader(owner) Long userId,
                                          @RequestBody CommentDto commentDto,
                                          @PathVariable Long itemId) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
