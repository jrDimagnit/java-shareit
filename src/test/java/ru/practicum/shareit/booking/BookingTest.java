package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BookingTest {
    @Test
    public void bookingTest() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStartDate(LocalDateTime.now());
        booking.setEndDate(LocalDateTime.now().plusSeconds(10));
        Booking newBooking = new Booking(booking.getId(), booking.getStartDate(), booking.getEndDate(),
                booking.getStatus(), booking.getBooker(), booking.getItem());
        booking.equals(newBooking);
        booking.hashCode();
        assertEquals(booking, newBooking);

    }
}
