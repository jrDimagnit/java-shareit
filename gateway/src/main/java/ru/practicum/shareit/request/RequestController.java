package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.BadParametersException;
import ru.practicum.shareit.request.dto.RequestDto;



@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;
    private final String owner = "X-Sharer-User-ID";
    private final String fromParams = "0";
    private final String sizeParams = "10";

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(owner) Long userId,
                                                @RequestBody RequestDto itemRequestDto) {
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUser(@RequestHeader(owner) Long userId,
                                            @RequestParam(defaultValue = fromParams) Integer from,
                                            @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size <= 0) {
            throw new BadParametersException("Неверные параметры сортировки");
        }
        return requestClient.getByUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(owner) Long userId, @PathVariable Long requestId) {
        return requestClient.getById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(owner) Long userId,
                                         @RequestParam(defaultValue = fromParams) Integer from,
                                         @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size < 0) {
            throw new BadParametersException("Неверные параметры сортировки");
        }
        return requestClient.getAll(userId, from, size);
    }
}
