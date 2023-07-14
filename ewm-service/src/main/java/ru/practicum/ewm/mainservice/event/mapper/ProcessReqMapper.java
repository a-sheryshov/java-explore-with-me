package ru.practicum.ewm.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.event.dto.ProcessReqResponseDto;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventRequestStatuses;
import ru.practicum.ewm.mainservice.eventrequest.mapper.EventRequestMapper;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;


import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProcessReqMapper {
    public static ProcessReqResponseDto toDto(List<EventRequest> requests) {
        List<EventReqRequestDto> mappedRequests = EventRequestMapper.toEventRequestDto(requests);

        return ProcessReqResponseDto.builder()
                .confirmedRequests(
                        mappedRequests.stream()
                                .filter(r -> r.getStatus().equals(EventRequestStatuses.CONFIRMED.name()))
                                .collect(Collectors.toList())
                )
                .rejectedRequests(
                        mappedRequests.stream()
                                .filter(r -> r.getStatus().equals(EventRequestStatuses.REJECTED.name()))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
