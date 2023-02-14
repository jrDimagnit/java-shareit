package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId, Sort sort, PageRequest pageRequest);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status, Sort sort, PageRequest pageRequest);

    List<Booking> findByBooker_IdAndStatusAndEndDateBefore(Long userId, BookingStatus status, LocalDateTime end,
                                                           Sort sort, PageRequest pageRequest);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "order by b.endDate desc ")
    List<Booking> findByOwnerId(Long userId, PageRequest pageRequest);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.status = ?2 order by b.endDate desc ")
    List<Booking> findByOwnerIdAndStatus(Long userId, BookingStatus status, PageRequest pageRequest);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndDateBefore(Long itemId, Long userId, BookingStatus status,
                                                                        LocalDateTime localDateTime);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.status = ?2 and b.endDate < ?3 order by b.endDate desc ")
    List<Booking> findByOwnerIdAndStatusIsBefore(Long userId, BookingStatus approved, LocalDateTime localDateTime,
                                                 PageRequest pageRequest);

    Booking findBookingByItem_IdAndStatusAndEndDateAfter(Long itemId, BookingStatus approved,
                                                         LocalDateTime localDateTime, Sort sort);

    Booking findBookingByItem_IdAndStatusAndEndDateBefore(Long itemId, BookingStatus approved,
                                                          LocalDateTime localDateTime, Sort sort);

}
