package ru.practicum.ewm.mainservice.eventrequest.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventRequestMapper {
    public static EventReqRequestDto toEventRequestDto(EventRequest eventRequest) {
        return EventReqRequestDto.builder()
                .id(eventRequest.getId())
                .created(eventRequest.getCreated())
                .eventId(eventRequest.getEvent().getId())
                .requesterId(eventRequest.getRequester().getId())
                .status(eventRequest.getStatus().getName())
                .build();
    }

    public static List<EventReqRequestDto> toEventRequestDto(List<EventRequest> eventRequest) {
        return eventRequest.stream()
                .map(EventRequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }
}
