package ru.practicum.ewm.mainservice.event.service;


import ru.practicum.ewm.mainservice.event.dto.*;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;

import java.util.List;

public interface EventPrivateService {

    EventDto create(EventCreateRequestDto dto, long userId);

    EventDto update(EventRequestDto dto, long userId, long eventId);

    List<EventShortDto> getAll(long userId, int from, int size);

    EventDto getById(long userId, long eventId);

    List<EventReqRequestDto> getRequests(long userId, long eventId);

    ProcessReqResponseDto processRequests(long userId, long eventId, ProcessReqRequestDto dto);

}
