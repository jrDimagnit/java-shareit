package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDto createBooking(BookingDto bookingDto, Long userId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new NotOwnerException("Неверная дата");
        }
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден!"));
        if (userId.equals(item.getOwnerId())) {
            throw new NotFoundException("Вещь недоступна для бронирования самим владельцем!");
        }
        if (!item.getAvailable()) {
            throw new NotOwnerException("Предмет не доступен");
        }
        Booking booking = bookingRepository.save(bookingMapper.toBooking(bookingDto, item, booker));
        log.debug("Бронирование сохранено!");
        return bookingMapper.fromBooking(booking);
    }

    @Transactional
    public BookingResponseDto approvedBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено!"));
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (!booking.getItem().getOwnerId().equals(owner.getId())) {
            throw new NotFoundException("Неверный пользователь!");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new NotOwnerException("Данный статус уже подтвержден!");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        log.debug("Изменение статуса бронирования!");
        return bookingMapper.fromBooking(bookingRepository.save(booking));
    }

    public BookingResponseDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено!"));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwnerId().equals(userId)) {
            return bookingMapper.fromBooking(booking);

        }
        throw new NotFoundException("Пользователь не найден!");
    }

    public List<BookingResponseDto> getByBookerIdAndState(Long userId, String status) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        BookingStatus statusCheck = checkStatus(status);
        if (statusCheck.equals(BookingStatus.PAST)) {
            return bookingRepository.findByBooker_IdAndStatusAndEndDateBefore(owner.getId(),
                            BookingStatus.APPROVED,
                            LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "endDate")).stream()
                    .map(bookingMapper::fromBooking)
                    .collect(Collectors.toList());
        }
        if (!statusCheck.equals(BookingStatus.ALL)) {
            return bookingRepository.findByBookerIdAndStatus(owner.getId(), statusCheck, Sort.by(Sort
                            .Direction.DESC, "endDate")).stream()
                    .map(bookingMapper::fromBooking)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByBookerId(owner.getId(), Sort.by(Sort.Direction.DESC, "endDate")).stream()
                .map(bookingMapper::fromBooking)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDto> getAllOwnerId(Long userId, String status) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        BookingStatus statusCheck = checkStatus(status);
        if (statusCheck.equals(BookingStatus.PAST)) {
            return bookingRepository.findByOwnerIdAndStatusIsBefore(owner.getId(), BookingStatus.APPROVED,
                            LocalDateTime.now()).stream()
                    .map(bookingMapper::fromBooking)
                    .collect(Collectors.toList());
        }
        if (!statusCheck.equals(BookingStatus.ALL)) {
            return bookingRepository.findByOwnerIdAndStatus(owner.getId(), statusCheck).stream()
                    .map(bookingMapper::fromBooking)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByOwnerId(owner.getId()).stream()
                .map(bookingMapper::fromBooking)
                .collect(Collectors.toList());
    }

    private BookingStatus checkStatus(String status) {
        switch (status) {
            case "REJECTED":
            case "CURRENT":
                return BookingStatus.REJECTED;
            case "APPROVED":
                return BookingStatus.APPROVED;
            case "PAST":
                return BookingStatus.PAST;
            case "WAITING":
                return BookingStatus.WAITING;
            case "ALL":
            case "FUTURE":
                return BookingStatus.ALL;
            default:
                throw new NotOwnerException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

}
