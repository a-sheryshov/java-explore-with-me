package ru.practicum.ewm.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.service.util.PublicServiceGetAllParameter;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.exception.InvalidPeriodException;
import ru.practicum.ewm.mainservice.util.CommonService;
import ru.practicum.ewm.mainservice.util.querydsl.EventQueryFilter;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitsRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventPublicServiceImpl implements EventPublicService {
    private final StatsClient statsClient;
    private final CommonService commonService;
    public static final String APP_NAME = "ewm-main-service";

    @Override
    public List<EventShortDto> getAll(PublicServiceGetAllParameter parameter) {
        List<Event> events;
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;

        checkRangePeriod(parameter.getRangeStart(), parameter.getRangeEnd());
        EventQueryFilter filter = fillFilter(parameter.getText(),
                parameter.getCategories(),
                parameter.getPaid(),
                parameter.getRangeStart(),
                parameter.getRangeEnd(),
                parameter.isOnlyAvailable());
        Pageable pageable = commonService.createPageableBySort(parameter.getSort(),
                parameter.getFrom(),
                parameter.getSize());

        events = commonService.findByFilter(pageable, filter);
        log.info("Response for events by filter was returned");

        confirmedRequests = commonService.findConfirmedRequests(events);
        views = commonService.findEventsViews(events);

        List<String> uris = events.stream().map(event ->  "/events/" + event.getId()).collect(Collectors.toList());
        uris.add("/events");
        statsClient.saveHits(HitsRequestDto.builder()
                .app(APP_NAME)
                .uris(uris)
                .ip(parameter.getIp())
                .timeStamp(LocalDateTime.now())
                .build());
        log.info("Saving stats");

        return EventMapper.toEventShortDto(events, confirmedRequests, views);
    }

    @Override
    public EventDto getById(long eventId, String ip) {
        Map<Long, Integer> views;
        List<EventRequest> confirmedRequests;

        Event event = commonService.findPublicEventOrThrow(eventId);
        log.info("Event id = {} returned", eventId);

        confirmedRequests = commonService.findConfirmedRequests(event);
        views = commonService.findEventViews(event);

        statsClient.saveHit(HitRequestDto.builder()
                .app(APP_NAME)
                .uri("/events/" + eventId)
                .ip(ip)
                .timeStamp(LocalDateTime.now())
                .build());
        log.info("Saving stats");

        return EventMapper.toEventDto(event, confirmedRequests, views);
    }

    private EventQueryFilter fillFilter(String text, List<Long> categories, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    boolean onlyAvailable) {
        return EventQueryFilter.builder()
                .onlyAvailable(onlyAvailable)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .categories(categories)
                .paid(paid)
                .text("%" + text.trim().toLowerCase() + "%")
                .build();
    }

    private void checkRangePeriod(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new InvalidPeriodException("Start time should be before end time");
        }
    }
}
