package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


@Service
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto, Item item, User user) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(),
                bookingDto.getEnd(), bookingDto.getStatus(), user, item);
    }

    public BookingResponseDto fromBooking(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto(booking.getId(), booking.getStartDate(),
                booking.getEndDate(), booking.getStatus(), new BookingItemDto(booking.getItem().getId(),
                booking.getItem().getName()), new BookerDto(booking.getBooker().getId()));
        return bookingResponseDto;
    }

    public LastNextBookingDto fromBookingShort(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new LastNextBookingDto(booking.getId(), booking.getBooker().getId());
    }
}
