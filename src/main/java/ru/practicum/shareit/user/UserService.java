package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.EmailException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private Set<String> baseEmail;
    UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUser() {
        return new ArrayList<>(userRepository.getAll());
    }

    public User createUser(User user) {
        if (baseEmail.contains(user.getEmail())) {
            throw new EmailException("Email занят!");
        } else {
            baseEmail.add(user.getEmail());
            log.debug("Проверка почты прошла успешно!");
            return userRepository.createUser(user);
        }
    }

    public User updateUser(User user) {
        if (baseEmail.contains(user.getEmail())) {
            throw new EmailException("Email занят!");
        }
        baseEmail.remove(getUserById(user.getId()).getEmail());
        log.debug("Старая почта пользователя с id {} удалена!", user.getId());
        baseEmail.add(user.getEmail());
        return userRepository.updateUser(user);
    }

    public void deleteUserById(Long id) {
        baseEmail.remove(getUserById(id).getEmail());
        log.debug("Старая почта пользователя с id {} удалена!", id);
        userRepository.deleteUserById(id);
    }
}
