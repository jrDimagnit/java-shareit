package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTest {
    @Test
    public void commentTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(new Item());
        comment.setUser(new User());
        comment.setText("test");
        comment.setCreated(LocalDateTime.now());
        Comment newComment = new Comment(comment.getId(), comment.getText(), comment.getUser(),
                comment.getItem(), comment.getCreated());
        comment.equals(newComment);
        comment.hashCode();
        assertEquals(comment, newComment);
    }
}
