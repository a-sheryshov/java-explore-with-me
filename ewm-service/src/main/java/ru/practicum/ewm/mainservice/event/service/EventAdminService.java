package ru.practicum.ewm.mainservice.event.service;

import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.service.util.AdminServiceGetAllQuery;

import java.util.List;

public interface EventAdminService {
    EventDto update(EventRequestDto dto, long eventId);

    List<EventDto> getAll(AdminServiceGetAllQuery parameter);
}
