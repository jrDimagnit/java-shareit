package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private ItemDto itemDto;
    private ItemWithCommentsDto item;
    private Long idNew = 1L;

    @BeforeEach
    public void startTest() {
        itemDto = new ItemDto(idNew, "name", "desc", true, null);
        item = new ItemWithCommentsDto(idNew, "name", "desc", true,
                new LastNextBookingDto(1L, 1L), new LastNextBookingDto(1L, 1L),
                new ArrayList<>());
    }

    @Test
    public void createItemTest() throws Exception {
        when(itemService.createItem(any(ItemDto.class), anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", idNew)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void updateItemTest() throws Exception {
        when(itemService.updateItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/{itemId}", idNew)
                        .header("X-Sharer-User-Id", idNew)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void getAllItemByUserIdTest() throws Exception {
        List<ItemWithCommentsDto> items = List.of(item);
        when(itemService.getAllItemByUserId(anyLong(),any()))
                .thenReturn(items);
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", idNew)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any(), anyLong()))
                .thenReturn(item);
        mockMvc.perform(get("/items/{itemId}", idNew)
                        .header("X-Sharer-User-Id", idNew)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void searchItemTest() throws Exception {
        List<ItemDto> items = List.of(itemDto);
        when(itemService.search(anyString()))
                .thenReturn(items);
        mockMvc.perform(get("/items/search?text=desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void addCommentsTest() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto(idNew, "text", "Name",
                LocalDateTime.now());
        CommentDto commentDto = new CommentDto(idNew, "text", LocalDateTime.now());
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentResponseDto);
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", idNew)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

}
