package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMaper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestsRepository itemRequestsRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMaper itemMaper;

    @Override
    public ItemRequestDto add(ItemRequestDto itemRequestDto, int userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь не найден");
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userId);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestMapper.toItemRequestDto(itemRequestsRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(int userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь не найден");
        List<ItemRequest> result = itemRequestsRepository.findAllByRequestorOrderByCreatedDesc(userId);
        result.forEach(itemRequest -> itemRequest.setItems(addAnswerToRequest(itemRequest.getId())));
        return itemRequestMapper
                .toItemRequestDto(result);
    }

    @Override
    public ItemRequestDto getRequestById(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest result = itemRequestsRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос с ID"));
        result.setItems(addAnswerToRequest(result.getId()));
        return itemRequestMapper.toItemRequestDto(result);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int from, int size, int userId) {
        List<ItemRequest> result;

        Pageable p = PageRequest.of(from, size);

        result = itemRequestsRepository.findAll(p)
                .stream().filter(itemRequest -> itemRequest.getRequestor() != userId)
                .collect(Collectors.toList());

        result.forEach(itemRequest -> itemRequest.setItems(addAnswerToRequest(itemRequest.getId())));
        return itemRequestMapper.toItemRequestDto(result);
    }

    public Set<ItemRequest.ShortItem> addAnswerToRequest(int requestId) {
        return itemMaper.toShortItem(itemRepository.findAllByRequestId(requestId));
    }
}
