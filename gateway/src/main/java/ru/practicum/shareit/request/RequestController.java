package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.BadParametersException;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;
    private final String owner = "X-Sharer-User-ID";

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(owner) Long userId,
                                                    @Valid @RequestBody RequestDto requestDto) {
        return requestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader(owner) Long userId) {
        return requestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(owner) Long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (from < 0 || size <= 0) {
            throw new BadParametersException("incorrect params");
        }
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable("id") Long requestId,
                                                 @RequestHeader(owner) Long userId) {
        return requestClient.getItemRequest(requestId, userId);
    }

}
