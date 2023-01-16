package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    ItemService itemService;

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
    public Collection<ItemDto> getAllItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.debug("Запрос предмета с id {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return text.isBlank() ? Collections.emptyList() : itemService.search(text);
    }
}
