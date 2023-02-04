package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private BookingDto bookingDto;
    private BookingResponseDto bookingResponseDto;
    private Long id = 1L;

    @BeforeEach
    public void startTest() {
        bookingDto = new BookingDto(1L, 1L, 1L, LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(100), BookingStatus.APPROVED);
        bookingResponseDto = new BookingResponseDto(1L, LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(100), BookingStatus.APPROVED,
                new BookingItemDto(1L, "name"), new BookerDto(1L));
    }

    @Test
    public void createBookingTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(bookingResponseDto);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", id)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void approvedBookingTest() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingResponseDto);
        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", 1L)
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void getByIdTest() throws Exception {
        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingResponseDto);
        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void getByBookerIdAndStateTest() throws Exception {
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);
        when(bookingService.getByBookerIdAndState(anyLong(), any(), any())).thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookings.get(0).getId()));
    }

    @Test
    public void getAllOwnerIdTest() throws Exception {
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);
        when(bookingService.getAllOwnerId(anyLong(), any(), any())).thenReturn(bookings);
        mockMvc.perform(get("/bookings/owner?size=-2")
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }
}
