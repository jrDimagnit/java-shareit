package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ItemService {
    private HashMap<Long, List<Long>> ownerItem;

    ItemRepository itemRepository;
    UserRepository userRepository;
    ItemMapper itemMapper;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        log.debug("Создание предмета {}", itemDto);
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден!");
        }
        Item item = itemRepository.createItem(itemMapper.toItem(itemDto, userId));
        ownerItem.compute(userId, (id, itemList) -> {
            if (itemList == null) {
                itemList = new ArrayList<>();
            }
            itemList.add(item.getId());
            return itemList;
        });
        return itemMapper.fromItem(item);
    }

    public ItemDto updateItem(ItemDto itemDto, Long userId) {
        if (findUserById(userId) == null || !ownerItem.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        if (ownerItem.get(userId).contains(itemDto.getId())) {
            log.debug("Обновление данных о предмете с id {}", itemDto.getId());
            return itemMapper.fromItem(itemRepository.updateItem(itemDto));
        } else {
            throw new NotOwnerException("Пользователь не найден!");
        }
    }

    public ItemDto getItemById(Long itemId) {
        log.debug("Запрос предмета с id {}", itemId);
        return itemMapper.fromItem(itemRepository.getById(itemId));
    }

    public List<ItemDto> getAllItemByUserId(Long userId) {
        List<ItemDto> getList = new ArrayList<>();
        for (Long id : ownerItem.get(userId)) {
            getList.add(getItemById(id));
        }
        return getList;
    }


    public User findUserById(Long userId) {
        log.debug("Поиск пользователя по id {}", userId);
        return userRepository.getUserById(userId);
    }

    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(Item -> itemMapper.fromItem(Item))
                .collect(Collectors.toList());
    }
}
