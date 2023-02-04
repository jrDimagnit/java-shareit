package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Transactional(readOnly = true)
public class ItemService {

    ItemRepository itemRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    ItemMapper itemMapper;
    CommentMapper commentMapper;
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;

    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long userId, Long requestId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Item item = itemRepository.save(itemMapper.toItem(itemDto, owner.getId(), requestId));
        log.debug("Предмет сохранен {}", itemDto);
        return itemMapper.fromItem(item);
    }

    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Item itemUpdate = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден!"));
        if (!itemUpdate.getOwnerId().equals(owner.getId())) {
            throw new NotFoundException("Неверный пользователь!");
        }
        if (itemDto.getName() != null) {
            itemUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemUpdate.setAvailable(itemDto.getAvailable());
        }
        itemUpdate = itemRepository.save(itemUpdate);
        log.debug("Обновление item {}", itemDto);
        return itemMapper.fromItem(itemUpdate);
    }

    @Transactional
    public ItemWithCommentsDto getItemById(Long itemId, Long userId) {
        log.debug("Запрос предмета с id {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден!"));
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        if (userId.equals(item.getOwnerId())) {
            LastNextBookingDto nextBooking = bookingMapper.fromBookingShort(bookingRepository
                    .findBookingByItem_IdAndStatusAndEndDateAfter(itemId, BookingStatus.APPROVED,
                            LocalDateTime.now(), Sort.by(DESC, "startDate")));
            LastNextBookingDto lastBooking = bookingMapper.fromBookingShort(bookingRepository
                    .findBookingByItem_IdAndStatusAndEndDateBefore(itemId, BookingStatus.APPROVED,
                            LocalDateTime.now(), Sort.by(ASC, "startDate")));
            return itemMapper.fromItemWithComments(item, comments.stream().map(commentMapper::fromComment)
                    .collect(Collectors.toList()), lastBooking, nextBooking);
        } else {
            return itemMapper.fromItemWithComments(item, comments.stream().map(commentMapper::fromComment)
                    .collect(Collectors.toList()), null, null);
        }
    }

    @Transactional
    public List<ItemWithCommentsDto> getAllItemByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        List<Item> responseItem = itemRepository.findItemsByOwnerId(user.getId(), Sort.by(ASC, "id"));
        List<ItemWithCommentsDto> itemsWithComments = new ArrayList<>();
        for (Item item : responseItem) {
            itemsWithComments.add(getItemById(item.getId(), userId));
        }
        return itemsWithComments;
    }

    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(Item -> itemMapper.fromItem(Item))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Boolean hasBooked = bookingRepository.existsBookingByItem_IdAndBooker_IdAndStatusAndEndDateBefore(itemId,
                userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (!hasBooked || hasBooked == null) {
            throw new NotOwnerException("Comment не может быть создан");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден!"));
        Comment comment = commentRepository.save(commentMapper.toComment(user, item, commentDto));
        log.debug("Добавлен Comment {}", comment);
        return commentMapper.fromComment(comment);
    }

}
