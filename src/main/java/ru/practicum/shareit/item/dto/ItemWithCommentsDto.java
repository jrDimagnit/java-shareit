package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemWithCommentsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastNextBookingDto lastBooking;
    private LastNextBookingDto nextBooking;
    private List<CommentResponseDto> comments = new ArrayList<>();
}
