package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private Long id = 1L;
    User user;
    ItemRequestDto requestDto;

    @BeforeEach
    public void startTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test@test.ru");
        requestDto = new ItemRequestDto();
        requestDto.setDescription("description");
    }

    @Test
    public void createItemRequestTest() {
        user.setEmail("test@test1.ru");
        Long userId = userService.createUser(user).getId();
        ItemRequestDto request = itemRequestService.createRequest(userId, requestDto);
        assertThat("description", equalTo(request.getDescription()));
    }

    @Test
    public void getByIdTest() {
        user.setEmail("test@test2.ru");
        Long userId = userService.createUser(user).getId();
        ItemRequestDto request = itemRequestService.createRequest(userId, requestDto);
        List<ItemRequestDto> requests = itemRequestService.getByUser(userId, PageRequest.of(0, 10));
        assertThat(request.getId(), equalTo(requests.get(0).getId()));
        assertThat(1, equalTo(requests.size()));
    }

    @Test
    public void getByRequestIdTest() {
        user.setEmail("test@test3.ru");
        Long userId = userService.createUser(user).getId();
        ItemRequestDto request = itemRequestService.createRequest(userId, requestDto);
        ItemRequestDto responseRequest = itemRequestService.getById(userId, request.getId());
        assertThat(request.getId(), equalTo(responseRequest.getId()));
        assertThat(responseRequest.getDescription(), equalTo(request.getDescription()));
    }

    @Test
    public void getAll() {
        User user1 = new User(0L, "name1", "test4@test.ru");
        Long userId1 = userService.createUser(user1).getId();
        User user2 = new User(0L, "name2", "test5@test.ru");
        Long userId2 = userService.createUser(user2).getId();

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(userId1, "test1");
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(userId2, "test2");
        Long requestId = itemRequestService.createRequest(userId1, itemRequestDto1).getId();
        Long requestId1 = itemRequestService.createRequest(userId2, itemRequestDto2).getId();
        ItemDto itemDto = itemMapper.fromItem(new Item(0L, "ItemName", "ItemDescription",
                true, requestId, userId1));
        itemService.createItem(itemDto, userId1, requestId).getId();

        List<ItemRequestDto> requests = itemRequestService.getAll(userId1, PageRequest.of(0, 10));

        assertThat(1, equalTo(requests.size()));
        assertThat(requestId1, equalTo(requests.get(0).getId()));
    }
}
