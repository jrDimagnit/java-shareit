package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_IdAndStatusAndEndDateBefore(Long userId, BookingStatus status, LocalDateTime end, Sort sort);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "order by b.endDate desc ")
    List<Booking> findByOwnerId(Long userId);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.status = ?2 order by b.endDate desc ")
    List<Booking> findByOwnerIdAndStatus(Long userId, BookingStatus status);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndDateBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime localDateTime);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.status = ?2 and b.endDate < ?3 order by b.endDate desc ")
    List<Booking> findByOwnerIdAndStatusIsBefore(Long userId, BookingStatus approved, LocalDateTime localDateTime);

    Booking findBookingByItem_IdAndStatusAndEndDateAfter(Long itemId, BookingStatus approved, LocalDateTime localDateTime, Sort sort);

    Booking findBookingByItem_IdAndStatusAndEndDateBefore(Long itemId, BookingStatus approved, LocalDateTime localDateTime, Sort sort);

}
