package ru.practicum.ewm.mainservice.user.service;

import ru.practicum.ewm.mainservice.user.dto.UserDto;
import ru.practicum.ewm.mainservice.user.dto.UserRequestDto;

import java.util.List;

public interface UserService {
    UserDto create(UserRequestDto dto);

    List<UserDto> get(List<Long> ids, int from, int size);

    void delete(long userId);
}
