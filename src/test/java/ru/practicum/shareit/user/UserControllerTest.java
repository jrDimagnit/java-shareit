package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private User user = new User();
    private Long userId = 1L;

    @BeforeEach
    public void startTest() {
        user.setName("name");
        user.setEmail("test@test.ru");
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.createUser(ArgumentMatchers.any(User.class)))
                .thenReturn(user);
        mockMvc.perform(post("/users")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void updateUserTest() throws Exception {
        user.setId(userId);
        when(userService.updateUser(ArgumentMatchers.any(User.class)))
                .thenReturn(user);
        mockMvc.perform(patch("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(user);
        mockMvc.perform(get("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void getAllUserTest() throws Exception {
        user.setId(userId);
        List<User> users = List.of(user);

        when(userService.getAllUser())
                .thenReturn(users);
        mockMvc.perform(get("/users", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId()));
    }

    @Test
    public void deleteUserByIdTest() throws Exception {
        mockMvc.perform(delete("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
