package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    @Test
    public void itemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setAvailable(true);
        item.setDescription("desc");
        item.setRequestId(1L);
        item.setOwnerId(1L);
        Item newItem = new Item(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getOwnerId(), item.getRequestId());
        item.equals(newItem);
        item.hashCode();
        assertEquals(item, newItem);
    }
}
