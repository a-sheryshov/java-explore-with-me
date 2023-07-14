package ru.practicum.ewm.mainservice.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.user.dto.UserDto;
import ru.practicum.ewm.mainservice.user.dto.UserRequestDto;
import ru.practicum.ewm.mainservice.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequestDto dto) {
        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return user;
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
