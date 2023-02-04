package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    ItemRequestMapper itemRequestMapper;
    ItemMapper itemMapper;

    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null) {
            throw new NotOwnerException("Отзыв не может быть создан");
        }
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        ItemRequest itemRequest = itemRequestRepository.save(itemRequestMapper.toItemRequest(itemRequestDto,
                owner.getId()));
        log.debug("Добавлен Запрос");
        return itemRequestMapper.fromItemRequest(itemRequest);
    }

    public List<ItemRequestDto> getByUser(Long userId, PageRequest pageRequest) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorOrderByCreatedDesc(pageRequest,
                owner.getId());
        return createListRequest(requests);
    }

    public ItemRequestDto getById(Long userId, Long requestId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден!"));
        ItemRequestDto requestDto = itemRequestMapper.fromItemRequest(itemRequest);
        requestDto.setItems(itemRepository.findItemsByRequestId(requestId)
                .stream()
                .map(itemMapper::fromItem)
                .collect(Collectors.toList()));
        ;
        return requestDto;
    }

    public List<ItemRequestDto> getAll(Long userId, PageRequest pageRequest) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorNotOrderByCreatedDesc(pageRequest,
                owner.getId());
        return createListRequest(requests);
    }

    private List<ItemRequestDto> createListRequest(List<ItemRequest> requests) {
        List<Item> items = itemRepository.findAllWithNonNullRequest();
        List<ItemRequestDto> itemRequestDto = new ArrayList<>();
        for (ItemRequest itemRequest : requests) {
            List<ItemDto> itemDto = new ArrayList<>();
            for (Item item : items) {
                if (Objects.equals(itemRequest.getId(), item.getRequestId())) {
                    itemDto.add(itemMapper.fromItem(item));
                }
            }
            ItemRequestDto dto = itemRequestMapper.fromItemRequest(itemRequest);
            dto.setItems(itemDto);
            itemRequestDto.add(dto);
        }
        return itemRequestDto;
    }
}
