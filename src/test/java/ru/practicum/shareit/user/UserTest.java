package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void userChangeTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("test@test.ru");
        user.setId(1L);
        User newUser = new User(user.getId(), user.getName(), user.getEmail());
        user.equals(newUser);
        user.hashCode();
        assertEquals(user, newUser);
    }
}
