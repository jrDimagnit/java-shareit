package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User requestor;
    private ItemRequest request;

    @BeforeEach
    public void startTest() {
        user = userRepository.save(new User(null, "test", "test@test.com"));
        requestor = userRepository.save(new User(null, "requestor", "requestor@email.com"));
        request = itemRequestRepository.save(new ItemRequest(null, "request", requestor.getId(),
                LocalDateTime.now()));
    }

    @AfterEach
    public void afterEachTest() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    public void findAllByRequestorOrderByCreatedDescTest() {
        List<ItemRequest> result = itemRequestRepository
                .findAllByRequestorOrderByCreatedDesc(PageRequest.of(0, 10), requestor.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(request.getRequestor(), result.get(0).getRequestor());
        assertEquals(request.getCreated(), result.get(0).getCreated());
    }

    @Test
    public void findAllTest() {
        Page<ItemRequest> result = itemRequestRepository.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(request.getRequestor(), result.getContent().get(0).getRequestor());
        assertEquals(request.getCreated(), result.getContent().get(0).getCreated());
    }
}
