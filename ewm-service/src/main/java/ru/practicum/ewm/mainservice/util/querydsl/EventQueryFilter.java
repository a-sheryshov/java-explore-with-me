package ru.practicum.ewm.mainservice.util.querydsl;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class EventQueryFilter {
    private final List<Long> userIds;
    private  final List<String> states;
    private final String text;
    private final List<Long> categories;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final boolean onlyAvailable;
}
