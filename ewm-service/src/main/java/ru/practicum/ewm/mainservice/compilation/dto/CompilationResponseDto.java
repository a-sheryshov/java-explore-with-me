package ru.practicum.ewm.mainservice.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;


import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CompilationResponseDto {
    private final Long id;
    private final List<EventShortDto> events;
    private final boolean pinned;
    private final String title;
}
