package ru.practicum.ewm.mainservice.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@Getter
@RequiredArgsConstructor
public class UserShortDto {
    private final Long id;
    private final String name;
}
