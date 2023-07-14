package ru.practicum.ewm.mainservice.event.service;

import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.model.EventStates;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    EventDto update(EventRequestDto dto, long eventId);

    List<EventDto> getAll(List<Long> userIds,
                              List<EventStates> states,
                              List<Long> categories,
                              LocalDateTime rangeStart,
                              LocalDateTime rangeEnd,
                              int from, int size);
}
