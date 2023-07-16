package ru.practicum.ewm.mainservice.event.service;

import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.service.util.PublicServiceGetAllParameter;

import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getAll(PublicServiceGetAllParameter parameter);

    EventDto getById(long eventId, String ip);
}
