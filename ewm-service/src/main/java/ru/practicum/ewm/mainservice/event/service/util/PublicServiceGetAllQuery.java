package ru.practicum.ewm.mainservice.event.service.util;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.mainservice.event.dto.EventSortBy;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PublicServiceGetAllQuery {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private EventSortBy sort;
    private int from;
    private int size;
    private String ip;
}
