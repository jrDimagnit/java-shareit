package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    Long id;
    Long itemId;
    Long bookerId;
    @FutureOrPresent
    LocalDateTime start;
    @Future
    LocalDateTime end;
    BookingStatus status;
}
