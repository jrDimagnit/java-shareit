package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.NotOwnerException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String owner = "X-Sharer-User-ID";
    private final String params = "ALL";
    private final String fromParams = "0";
    private final String sizeParams = "10";

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(owner) Long userId,
                                            @Valid @RequestBody BookingDto bookingDto) {
        bookingDto.setStatus(BookingStatus.WAITING);
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approvedBooking(@RequestHeader(owner) Long userId,
                                              @RequestParam Boolean approved,
                                              @PathVariable Long bookingId) {
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(owner) Long userId,
                                      @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingResponseDto> getByBookerIdAndState(@RequestHeader(owner) Long userId,
                                                          @RequestParam(defaultValue = params) String state,
                                                          @RequestParam(defaultValue = fromParams) Integer from,
                                                          @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size <= 0) {
            throw new NotOwnerException("Неверные параметры сортировки");
        }
        return bookingService.getByBookerIdAndState(userId, state, PageRequest.of(from / size, size));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllOwnerId(@RequestHeader(owner) Long userId,
                                                  @RequestParam(defaultValue = params) String state,
                                                  @RequestParam(defaultValue = fromParams) Integer from,
                                                  @RequestParam(defaultValue = sizeParams) Integer size) {
        if (from < 0 || size <= 0) {
            throw new NotOwnerException("Неверные параметры сортировки");
        }
        return bookingService.getAllOwnerId(userId, state, PageRequest.of(from / size, size));
    }
}
