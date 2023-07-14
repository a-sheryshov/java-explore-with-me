package ru.practicum.ewm.mainservice.event.service;

import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.dto.EventSortBy;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getAll(String text,
                               List<Long> categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               boolean onlyAvailable,
                               EventSortBy sort,
                               int from, int size,
                               String ip);

    EventDto getById(long eventId, String ip);
}
