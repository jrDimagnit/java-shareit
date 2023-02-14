package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private Long id = 1L;
    User user;
    User owner;
    Item item;
    BookingDto bookingDto;

    @BeforeEach
    public void startTest() {
        user = new User(id, "name", "testtest@test.ru");
        owner = new User(id + 1, "name2", "test1test@Test.ru");
        item = new Item(id, "test", "description", true, id + 1, null);
        bookingDto = new BookingDto();
        bookingDto.setItemId(id);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setId(id);
        bookingDto.setEnd(LocalDateTime.now().plusHours(1));
        bookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void createBookingTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        Long itemId = itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        BookingResponseDto result = bookingService.createBooking(bookingDto, ownerId);

        assertThat(itemId, equalTo(result.getItem().getId()));
        assertThat(ownerId, equalTo(result.getBooker().getId()));
        assertThat(BookingStatus.WAITING, equalTo(result.getStatus()));
    }

    @Test
    public void createBookingFailDateTest() {
        bookingDto.setEnd(LocalDateTime.now().minusHours(1));
        Long userId = userService.createUser(user).getId();
        userService.createUser(owner);
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        assertThrows(NotOwnerException.class, () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    public void createBookingFailTest() {
        Long userId = userService.createUser(user).getId();
        userService.createUser(owner);
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    public void approvedBookingTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingService.createBooking(bookingDto, ownerId);
        BookingResponseDto result = bookingService.approvedBooking(userId, id, true);
        assertThat(BookingStatus.APPROVED, equalTo(result.getStatus()));
    }

    @Test
    public void approvedBookingFailNotOwnerTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingService.createBooking(bookingDto, ownerId);
        assertThrows(NotFoundException.class, () -> bookingService.approvedBooking(ownerId, id, true));
    }

    @Test
    public void getByIdTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingService.createBooking(bookingDto, ownerId);
        BookingResponseDto result = bookingService.getById(ownerId, id);
        assertThat(ownerId, equalTo(result.getBooker().getId()));
        assertThat(id, equalTo(result.getId()));
    }

    @Test
    public void getByBookerIdAndStateTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getByBookerIdAndState(ownerId, "APPROVED",
                PageRequest.of(0, 10));
        assertThat(1, equalTo(result.size()));
        assertThat(bookingDto.getId(), equalTo(result.get(0).getId()));

    }

    @Test
    public void getBookerIdAndPastTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getByBookerIdAndState(ownerId, "PAST",
                PageRequest.of(0, 10));
        assertThat(0, equalTo(result.size()));
    }

    @Test
    public void getBookerIdAndAllTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getByBookerIdAndState(ownerId, "ALL",
                PageRequest.of(0, 10));
        assertThat(1, equalTo(result.size()));
    }

    @Test
    public void getAllOwnerIdTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setEnd(LocalDateTime.now().minusHours(10));
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getAllOwnerId(userId, "PAST",
                PageRequest.of(0, 10));
        assertThat(1, equalTo(result.size()));
        assertThat(bookingDto.getId(), equalTo(result.get(0).getId()));
    }

    @Test
    public void getAllOwnerIdALLTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null).getId();
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setEnd(LocalDateTime.now().minusHours(10));
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getAllOwnerId(userId, "ALL",
                PageRequest.of(0, 10));
        assertThat(1, equalTo(result.size()));
    }

    @Test
    public void getAllOwnerIdWaitingTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setEnd(LocalDateTime.now().minusHours(10));
        bookingService.createBooking(bookingDto, ownerId);
        List<BookingResponseDto> result = bookingService.getAllOwnerId(userId, "WAITING",
                PageRequest.of(0, 10));
        assertThat(0, equalTo(result.size()));
    }

    @Test
    public void createBookingNotAvailableTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        item.setAvailable(false);
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        assertThrows(NotOwnerException.class, () -> bookingService.createBooking(bookingDto, ownerId));
    }

    @Test
    public void approvedBookingFailTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        bookingDto.setStatus(BookingStatus.APPROVED);
        Long idBooking = bookingService.createBooking(bookingDto, ownerId).getId();
        assertThrows(NotOwnerException.class, () -> bookingService.approvedBooking(userId, idBooking, false));
    }

    @Test
    public void approvedBookingRejectTest() {
        Long userId = userService.createUser(user).getId();
        Long ownerId = userService.createUser(owner).getId();
        itemService.createItem(itemMapper.fromItem(item), userId, null);
        Long idBooking = bookingService.createBooking(bookingDto, ownerId).getId();
        BookingResponseDto newBooking = bookingService.approvedBooking(userId, idBooking, false);
        assertThat(newBooking.getStatus(), equalTo(BookingStatus.REJECTED));
    }

}
