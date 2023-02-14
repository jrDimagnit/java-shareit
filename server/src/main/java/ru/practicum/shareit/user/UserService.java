package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
    }

    public List<User> getAllUser() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Transactional
    public User createUser(User user) {
        user = userRepository.save(user);
        log.debug("Пользователь {} создан", user);
        return user;
    }

    @Transactional
    public User updateUser(User user) {
        User userUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userUpdate.setName(user.getName());
        }
        userRepository.save(userUpdate);
        log.debug("Пользователь {} обновлен", user);
        return userUpdate;
    }

    @Transactional
    public void deleteUserById(Long id) {
        User userDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        userRepository.deleteById(userDelete.getId());
        log.debug("Старая почта пользователя с id {} удалена!", id);
    }
}
