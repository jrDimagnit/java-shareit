package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestTest {

    @Test
    public void itemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(1L);
        ItemRequest newRequest = new ItemRequest(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequestor(), itemRequest.getCreated());
        itemRequest.equals(newRequest);
        itemRequest.hashCode();
        assertEquals(itemRequest, newRequest);
    }
}
