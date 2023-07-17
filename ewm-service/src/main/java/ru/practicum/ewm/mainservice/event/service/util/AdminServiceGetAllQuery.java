package ru.practicum.ewm.mainservice.event.service.util;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.mainservice.event.model.EventStates;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminServiceGetAllQuery {
    private List<Long> userIds;
    private List<EventStates> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private int from;
    private int size;
}

