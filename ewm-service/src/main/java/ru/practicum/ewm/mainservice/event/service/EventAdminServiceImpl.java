package ru.practicum.ewm.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.dto.EventSortBy;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.model.EventStates;
import ru.practicum.ewm.mainservice.event.service.util.AdminServiceGetAllParameter;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.eventrequest.repository.EventRequestRepository;
import ru.practicum.ewm.mainservice.util.CommonService;
import ru.practicum.ewm.mainservice.util.querydsl.EventQueryFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventAdminServiceImpl implements EventAdminService {
    private final CommonService commonService;
    private final EventRequestRepository eventRequestRepository;
    private static final int MIN_HOURS_BEFORE_EVENT = 1;

    @Override
    @Transactional
    public EventDto update(EventRequestDto dto, long eventId) {
        Map<Long, Integer> views;
        List<EventRequest> confirmedRequests;
        commonService.checkEventDate(dto, MIN_HOURS_BEFORE_EVENT);
        Event event = commonService.findEventOrThrow(eventId);

        event = commonService.update(event, dto);
        confirmedRequests = eventRequestRepository.findConfirmedRequests(event.getId());
        views = commonService.findEventViews(event);
        log.info("Event id = {} updated by admin", eventId);
        return EventMapper.toEventDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventDto> getAll(AdminServiceGetAllParameter parameter) {
        List<Event> events;
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;

        Pageable pageable = commonService.createPageableBySort(EventSortBy.EVENT_DATE,
                parameter.getFrom(), parameter.getSize());
        EventQueryFilter filter = fillFilter(parameter.getUserIds(),
                parameter.getStates(),
                parameter.getCategories(),
                parameter.getRangeStart(),
                parameter.getRangeEnd());

        events = commonService.findByFilter(pageable, filter);
        confirmedRequests = commonService.findConfirmedRequests(events);
        views = commonService.findEventsViews(events);

        return EventMapper.toEventDto(events, confirmedRequests, views);
    }

    private EventQueryFilter fillFilter(List<Long> userIds,
                                    List<EventStates> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd) {
        return EventQueryFilter.builder()
                .userIds(userIds)
                .states(states == null ? null : states.stream().map(Enum::name).collect(Collectors.toList()))
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .categories(categories)
                .build();
    }
}
