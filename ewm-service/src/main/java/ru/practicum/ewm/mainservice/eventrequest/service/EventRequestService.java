package ru.practicum.ewm.mainservice.eventrequest.service;

import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;

import java.util.List;

public interface EventRequestService {
    EventReqRequestDto create(long userId, long eventId);

    List<EventReqRequestDto> getAll(long userId);

    EventReqRequestDto cancel(long userId, long requestId);

}
