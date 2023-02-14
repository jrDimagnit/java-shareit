package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private Long id = 1L;

    User booker;
    User user;
    ItemDto item;

    @Test
    public void createItemTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test21@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        Long userId = userService.createUser(user).getId();
        ItemDto result = itemService.createItem(item, userId, null);
        assertThat(item.getDescription(), equalTo(result.getDescription()));
        assertThat(item.getName(), equalTo(result.getName()));
    }

    @Test
    public void updateItemTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test2@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        Long userId = userService.createUser(user).getId();
        ItemDto itemDto = itemService.createItem(item, userId, null);
        item.setName("updateName");
        item.setId(itemDto.getId());
        ItemDto result = itemService.updateItem(item, userId);
        assertThat(item.getDescription(), equalTo(result.getDescription()));
        assertThat(item.getName(), equalTo(result.getName()));
    }

    @Test
    public void getItemByIdTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test3@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        Long userId = userService.createUser(user).getId();
        Long itemDtoId = itemService.createItem(item, userId, null).getId();
        ItemWithCommentsDto result = itemService.getItemById(itemDtoId, userId);
        assertThat(item.getDescription(), equalTo(result.getDescription()));
        assertThat(item.getName(), equalTo(result.getName()));
    }

    @Test
    public void getAllItemByUserIdTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test40@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        Long userId = userService.createUser(user).getId();
        Long itemDtoId = itemService.createItem(item, userId, null).getId();
        List<ItemWithCommentsDto> result = itemService.getAllItemByUserId(userId,PageRequest.of(0,20));
        assertThat(1, equalTo(result.size()));
        assertThat(itemDtoId, equalTo(result.get(0).getId()));

    }

    @Test
    public void searchTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test50@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("info");
        Long userId = userService.createUser(user).getId();
        ItemDto itemDto = itemService.createItem(item, userId, null);
        List<ItemDto> result = itemService.search("info");
        assertThat(1, equalTo(result.size()));
        assertThat(itemDto, equalTo(result.get(0)));
    }

    @Test
    public void addCommentTest() {
        user = new User();
        user.setName("test");
        user.setEmail("test6@test.ru");
        item = new ItemDto();
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        booker = new User();
        booker.setName("booker");
        booker.setEmail("test1@test.ru");
        Long userId = userService.createUser(user).getId();
        ItemDto itemDto = itemService.createItem(item, userId, null);
        Long bookerId = userService.createUser(booker).getId();
        BookingDto bookingDto = new BookingDto(null, itemDto.getId(), bookerId, LocalDateTime.now().plusNanos(3),
                LocalDateTime.now().plusNanos(10), BookingStatus.APPROVED);
        BookingResponseDto bookingResult = bookingService.createBooking(bookingDto, bookerId);
        CommentDto comment = new CommentDto(null, "desc", LocalDateTime.now().minusHours(1));
        CommentResponseDto result = itemService.addComment(bookerId, itemDto.getId(), comment);
        assertThat(result.getAuthorName(), equalTo(booker.getName()));
    }
}
