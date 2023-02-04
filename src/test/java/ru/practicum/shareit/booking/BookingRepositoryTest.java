package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void beforeEachTest() {
        user = userRepository.save(new User(null, "user", "test@email.com"));
        booker = userRepository.save(new User(null, "booker", "test1@email.com"));
        item = itemRepository.save(new Item(null, "name", "description", true,
                user.getId(), null));
        booking = bookingRepository.save(new Booking(null, LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusHours(1), BookingStatus.APPROVED, booker, item));

    }

    @AfterEach
    public void afterEachTest() {
        userRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void findByBookerIdTest() {
        List<Booking> result = bookingRepository.findByBookerId(booker.getId(),
                Sort.by(Sort.Direction.DESC, "endDate"), PageRequest.of(0, 10));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    public void findByBookerIdAndStatusTest() {
        List<Booking> result = bookingRepository.findByBookerIdAndStatus(booker.getId(), BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "endDate"), PageRequest.of(0, 10));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    public void findByBooker_IdAndStatusAndEndDateBeforeTest() {
        List<Booking> result = bookingRepository.findByBooker_IdAndStatusAndEndDateBefore(booker.getId(),
                BookingStatus.APPROVED, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "endDate"),
                PageRequest.of(0, 10));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByOwnerIdTest() {
        List<Booking> result = bookingRepository.findByOwnerId(user.getId(), PageRequest.of(0, 10));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    public void findByOwnerIdAndStatusTest() {
        List<Booking> result = bookingRepository.findByOwnerIdAndStatus(user.getId(), BookingStatus.APPROVED,
                PageRequest.of(0, 10));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    public void findByOwnerIdAndStatusIsBefore() {
        List<Booking> result = bookingRepository.findByOwnerIdAndStatusIsBefore(user.getId(), BookingStatus.APPROVED,
                LocalDateTime.now().plusDays(2), PageRequest.of(0, 10));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

}
