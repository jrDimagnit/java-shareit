package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private ItemRequestDto itemRequestDto;
    private final Long id = 1L;

    @BeforeEach
    public void startTest() {
        itemRequestDto = new ItemRequestDto(id, "Description", LocalDateTime.now(), new ArrayList<>());
    }

    @Test
    public void createRequestTest() throws Exception {
        ItemRequestDto itemRequestDtoNew = new ItemRequestDto(id, "Description");
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(itemRequestDtoNew);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", id)
                        .content(objectMapper.writeValueAsString(itemRequestDtoNew))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDtoNew.getId()));
    }

    @Test
    public void getByRequestIdTest() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/{requestId}", id)
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()));
    }

    @Test
    public void getRequestByUserId() throws Exception {
        List<ItemRequestDto> items = List.of(itemRequestDto);
        when(itemRequestService.getByUser(anyLong(), any()))
                .thenReturn(items);
        mockMvc.perform(get("/requests", id)
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(items.get(0).getId()));
    }

    @Test
    public void getAllTest() throws Exception {
        List<ItemRequestDto> items = List.of(itemRequestDto);
        when(itemRequestService.getAll(anyLong(), any()))
                .thenReturn(items);
        mockMvc.perform(get("/requests/all", id)
                        .header("X-Sharer-User-Id", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(items.get(0).getId()));
    }
}
