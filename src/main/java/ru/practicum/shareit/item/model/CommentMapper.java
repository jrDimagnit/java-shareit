package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Service
public class CommentMapper {

    public Comment toComment(User user, Item item, CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getText(), user, item, LocalDateTime.now());
    }

    public CommentResponseDto fromComment(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getText()
                , comment.getUser().getName(), comment.getCreated());

    }
}
