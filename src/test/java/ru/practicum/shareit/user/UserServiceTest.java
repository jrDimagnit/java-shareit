package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {
    private UserService userService;
    private User user;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void startTest() {
        userService = new UserService(userRepository);
        user = new User();
        user.setName("name");
        user.setEmail("test@test.ru");
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        User actualUser = userService.createUser(user);
        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void updateUserAndFindTest() {
        user.setName("newName");
        user.setId(1L);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));
        User userRes = userService.updateUser(user);
        assertEquals(userRes.getId(), 1);
        assertEquals(userRes.getName(), user.getName());

        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }
}
