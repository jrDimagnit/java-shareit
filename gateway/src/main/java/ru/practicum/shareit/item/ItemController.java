package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;


    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(USER_ID) Long ownerId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(required = false) Integer size) {
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) Long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                         @RequestHeader(USER_ID) Long userId) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearchQuery(@RequestParam String text,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(required = false) Integer size) {
        return itemClient.getItemsBySearchQuery(text, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDto commentDto,
                                                @RequestHeader(USER_ID) Long userId,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
