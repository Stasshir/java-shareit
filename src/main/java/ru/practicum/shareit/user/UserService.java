package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    List<UserDto> getAll();

    UserDto delete(int id);

    UserDto getById(int id);

    UserDto updateById(int id, UserDto userDto);

   // void checkUserToId(int id);
}
