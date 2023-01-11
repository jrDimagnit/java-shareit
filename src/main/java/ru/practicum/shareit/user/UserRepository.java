package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.NotFoundException;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class UserRepository {
    private Long id = 0L;
    private HashMap<Long, User> base = new HashMap<>();

    public User getUserById(Long id) {
        if (!base.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return base.get(id);
    }

    public Collection<User> getAll() {
        return base.values();
    }

    public User createUser(User user) {
        user.setId(++id);
        base.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getName() == null) {
            user.setName(base.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(base.get(user.getId()).getEmail());
        }
        base.put(user.getId(), user);
        return user;
    }

    public void deleteUserById(Long id) {
        base.remove(id);
    }

}
