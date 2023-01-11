package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Validated(Create.class) User user) {
        log.debug("Создание пользователя {}", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody @Validated(Update.class) User user, @PathVariable Long userId) {
        user.setId(userId);
        log.debug("Обновление данных пользователя с id {}", userId);
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        log.debug("Запрос данных пользователя с id {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAllUser() {
        log.debug("Запрос всех пользователей!");
        return userService.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Запрос на удаление пользователя с id {}", userId);
        userService.deleteUserById(userId);
    }
}
