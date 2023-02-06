package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String owner = "X-Sharer-User-ID";
    private final String fromParams = "0";
    private final String sizeParams = "10";

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(owner) Long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getByUser(@RequestHeader(owner) Long userId,
                                          @RequestParam(defaultValue = fromParams) Integer from,
                                          @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size <= 0) {
            throw new NotOwnerException("Неверные параметры сортировки");
        }
        return itemRequestService.getByUser(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(owner) Long userId, @PathVariable Long requestId) {
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(owner) Long userId,
                                       @RequestParam(defaultValue = fromParams) Integer from,
                                       @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size < 0) {
            throw new NotOwnerException("Неверные параметры сортировки");
        }
        return itemRequestService.getAll(userId, PageRequest.of(from / size, size));
    }
}
