package ru.practicum.ewm.mainservice.event.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventRequestStatuses;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ProcessReqRequestDto {
    @NotEmpty
    private final List<Long> requestIds;
    @NotNull
    private final EventRequestStatuses status;
}
