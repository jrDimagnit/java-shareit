package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.util.ArrayList;
import java.util.List;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithCommentsDto {
    Long id;
    String name;
    String description;
    Boolean available;
    LastNextBookingDto lastBooking;
    LastNextBookingDto nextBooking;
    List<CommentResponseDto> comments = new ArrayList<>();
}
