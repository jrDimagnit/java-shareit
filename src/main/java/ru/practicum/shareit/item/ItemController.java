package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;

import javax.validation.Valid;
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
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Validated(Create.class) ItemDto itemDto) {
        log.debug("Создание предмета {} от ползователя с id {}", itemDto, userId);
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId, @RequestBody @Validated(Update.class) ItemDto itemDto) {
        itemDto.setId(itemId);
        log.debug("Запрос на изменения данных предмета с id {} от пользователя с id {}", itemDto.getId(), userId);
        return itemService.updateItem(itemDto, userId);
    }

    @GetMapping
    public Collection<ItemWithCommentsDto> getAllItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long itemId) {
        log.debug("Запрос предмета с id {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return text.isBlank() ? Collections.emptyList() : itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComments(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody CommentDto commentDto,
                                          @PathVariable Long itemId) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
