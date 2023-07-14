package ru.practicum.ewm.mainservice.event.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ProcessReqResponseDto {
    private final List<EventReqRequestDto> confirmedRequests;
    private final List<EventReqRequestDto> rejectedRequests;
}
