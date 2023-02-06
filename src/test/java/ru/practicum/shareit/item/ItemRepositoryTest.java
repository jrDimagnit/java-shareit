package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    User user;
    Item item;

    @BeforeEach
    public void startTest() {
        user = userRepository.save(new User(null, "test", "test@test.com"));
        item = itemRepository.save(new Item(null, "name", "description",
                true, user.getId(), null));
    }

    @AfterEach
    public void afterEachTest() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void findItemsByOwnerIdTest() {
        List<Item> result = itemRepository.findItemsByOwnerId(user.getId(), Sort.by(DESC, "id"));
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findItemsByRequestIdTest() {
        List<Item> result = itemRepository.findItemsByRequestId(item.getRequestId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
