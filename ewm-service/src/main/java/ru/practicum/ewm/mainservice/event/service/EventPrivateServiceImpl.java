package ru.practicum.ewm.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.category.model.Category;
import ru.practicum.ewm.mainservice.event.dto.*;
import ru.practicum.ewm.mainservice.event.mapper.ProcessReqMapper;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.model.EventStates;
import ru.practicum.ewm.mainservice.event.model.Location;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventRequestStatuses;
import ru.practicum.ewm.mainservice.eventrequest.mapper.EventRequestMapper;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequestStatus;
import ru.practicum.ewm.mainservice.eventrequest.repository.EventRequestRepository;
import ru.practicum.ewm.mainservice.user.model.User;
import ru.practicum.ewm.mainservice.util.CommonService;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;
import ru.practicum.ewm.mainservice.category.service.CategoryService;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.mapper.LocationMapper;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final CommonService commonService;
    private final CategoryService categoryService;


    @Override
    @Transactional
    public EventDto create(EventCreateRequestDto dto, long userId) {
        commonService.checkEventDate(dto, 2);
        User initiator = commonService.findUserOrThrow(userId);
        Category category = categoryService.findCategoryOrThrow(dto.getCategoryId());
        Location location = commonService.findLocationOrSave(LocationMapper.toLocation(dto));
        Event event = EventMapper.toEvent(dto, category, location, initiator);

        event.setState(commonService.findEventStateOrThrow(EventStates.PENDING));
        event.setCreatedOn(LocalDateTime.now());
        event = eventRepository.save(event);
        log.info("Event id = {} created", event.getId());

        return EventMapper.toEventDto(event, List.of(), Map.of());
    }

    @Override
    @Transactional
    public EventDto update(EventRequestDto dto, long userId, long eventId) {
        commonService.checkEventDate(dto, 2);
        Map<Long, Integer> views;
        List<EventRequest> confirmedRequests;
        User user = commonService.findUserOrThrow(userId);
        Event event = commonService.findEventOrThrow(eventId);

        checkInitiator(user, event);
        checkUpdateAvailable(event);

        event = commonService.update(event, dto);
        confirmedRequests = commonService.findConfirmedRequests(event);
        views = commonService.findEventViews(event);
        log.info("Event id = {} updated by user id = {}", eventId, userId);

        return EventMapper.toEventDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventShortDto> getAll(long userId, int from, int size) {
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;
        User initiator = commonService.findUserOrThrow(userId);
        Pageable pageable = commonService.createPageable("eventDate", from, size);
        List<Event> events = eventRepository.findAllByInitiator(initiator, pageable);
        confirmedRequests = commonService.findConfirmedRequests(events);
        views = commonService.findEventsViews(events);
        log.info("Event list returned for user id = {} ", initiator.getId());

        return EventMapper.toEventShortDto(events, confirmedRequests, views);
    }

    @Override
    public EventDto getById(long userId, long eventId) {
        Map<Long, Integer> views;
        List<EventRequest> confirmedRequests;
        commonService.findUserOrThrow(userId);

        Event event = commonService.findEventOrThrow(eventId);
        confirmedRequests = commonService.findConfirmedRequests(event);
        views = commonService.findEventViews(event);

        return EventMapper.toEventDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventReqRequestDto> getRequests(long userId, long eventId) {
        List<EventRequest> requests;
        User user = commonService.findUserOrThrow(userId);
        Event event = commonService.findEventOrThrow(eventId);
        checkInitiator(user, event);

        requests = findEventRequestsByEvent(event);
        log.info("Event requests returned for event c id = {} ", eventId);

        return EventRequestMapper.toEventRequestDto(requests);
    }

    @Override
    @Transactional
    public ProcessReqResponseDto processRequests(long userId, long eventId, ProcessReqRequestDto dto) {
        User user = commonService.findUserOrThrow(userId);
        Event event = commonService.findEventOrThrow(eventId);
        checkInitiator(user, event);

        List<EventRequest> requests = findEventRequestsByIds(dto.getRequestIds());
        checkProcessRequestsAvailable(
                event,
                commonService.findConfirmedRequests(event)
        );
        if (requests.isEmpty()) return ProcessReqResponseDto.builder().build();
        requests = filterRequestsByPending(requests);

        switch (dto.getStatus()) {
            case REJECTED:
                rejectRequests(requests);
                break;
            case CONFIRMED:
                filterAndProcessRequests(requests, event);
                break;
        }

        return ProcessReqMapper.toDto(requests);
    }

    private void filterAndProcessRequests(List<EventRequest> requests, Event event) {
        if (event.getParticipantLimit() == 0) {
            confirmRequests(requests);
        } else {
            int limit = event.getParticipantLimit() - commonService.findConfirmedRequests(event).size();

            if (requests.size() <= limit) {
                confirmRequests(requests);
            } else if (limit > 0) {
                confirmRequests(requests.subList(0, limit - 1));
                rejectRequests(requests.subList(limit, requests.size()));
            } else {
                rejectRequests(requests);
            }
        }
    }

    private void confirmRequests(List<EventRequest> requests) {
        EventRequestStatus stat = commonService.findRequestStatusOrThrow(EventRequestStatuses.CONFIRMED);
        requests.forEach(request -> request.setStatus(stat));
    }

    private void rejectRequests(List<EventRequest> requests) {
        EventRequestStatus stat = commonService.findRequestStatusOrThrow(EventRequestStatuses.REJECTED);
        requests.forEach(request -> request.setStatus(stat));
    }

    private List<EventRequest> filterRequestsByPending(List<EventRequest> requests) {
        return requests.stream()
                .filter(r -> r.getStatus().getName().equals(EventRequestStatuses.PENDING.name()))
                .collect(Collectors.toList());
    }

    private List<EventRequest> findEventRequestsByEvent(Event event) {
        return eventRequestRepository.findAllByEvent(event);
    }

    private List<EventRequest> findEventRequestsByIds(List<Long> requestIds) {
        return eventRequestRepository.findAllByIdIn(requestIds);
    }

    private void checkInitiator(User user, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new OperationFailedException(
                    "Could be modified only by creator"
            );
        }
    }

    private void checkUpdateAvailable(Event event) {
        if (event.getState().getName().equals(EventStates.PUBLISHED.name())) {
            throw new OperationFailedException(
                    "Could not modify published events"
            );
        }
    }

    private void checkProcessRequestsAvailable(Event event, List<EventRequest> confirmedRequests) {
        if (event.getParticipantLimit() - confirmedRequests.size() < 1) {
            throw new OperationFailedException(
                    "Participant limit reached"
            );
        }
    }
}
