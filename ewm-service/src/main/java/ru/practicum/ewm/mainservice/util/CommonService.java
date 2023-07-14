package ru.practicum.ewm.mainservice.util;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.category.service.CategoryService;
import ru.practicum.ewm.mainservice.compilation.model.Compilation;
import ru.practicum.ewm.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.ewm.mainservice.event.dto.EventCreateRequestDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.dto.EventSortBy;
import ru.practicum.ewm.mainservice.event.dto.StateAction;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.model.EventState;
import ru.practicum.ewm.mainservice.event.model.EventStates;
import ru.practicum.ewm.mainservice.event.model.Location;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;
import ru.practicum.ewm.mainservice.event.repository.EventStateRepository;
import ru.practicum.ewm.mainservice.event.repository.LocationRepository;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventRequestStatuses;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequestStatus;
import ru.practicum.ewm.mainservice.eventrequest.repository.EventRequestRepository;
import ru.practicum.ewm.mainservice.eventrequest.repository.EventRequestStatusRepository;
import ru.practicum.ewm.mainservice.user.model.User;
import ru.practicum.ewm.mainservice.user.repository.UserRepository;
import ru.practicum.ewm.mainservice.exception.ObjectNotExistsException;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.mainservice.event.mapper.LocationMapper;
import ru.practicum.ewm.mainservice.exception.InvalidPeriodException;
import ru.practicum.ewm.mainservice.util.querydsl.EventQueryFilter;
import ru.practicum.ewm.mainservice.util.querydsl.QueryPredicates;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static ru.practicum.ewm.mainservice.event.model.QEvent.event;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class CommonService {
    private final LocationRepository locationRepository;
    private final EventStateRepository eventStateRepository;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final CompilationRepository compilationRepository;
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestStatusRepository eventRequestStatusRepository;
    private final StatsClient statsClient;

    //VALIDATION
    public void checkEventDate(EventRequestDto dto, int hours) {
        if (dto.getEventDate() != null
                && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new InvalidPeriodException("Too late to update event");
        }
    }

    public void checkEventDate(EventCreateRequestDto dto, int hours) {
        if (dto.getEventDate() != null
                && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new InvalidPeriodException("Too late to create event");
        }
    }

    //MODIFY OBJ
    public Event update(Event event, EventRequestDto dto) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategoryId() != null) event.setCategory(categoryService.findCategoryOrThrow(dto.getCategoryId()));
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) updateLocation(event, dto);
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getStateAction() != null) updateState(event, dto);
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) event.setTitle(dto.getTitle());
        return event;
    }

    private void updateState(Event event, EventRequestDto dto) {
        StateAction action = dto.getStateAction();
        switch (action) {
            case CANCEL_REVIEW:
                updateToCancel(event);
                break;
            case PUBLISH_EVENT:
                updateToPublish(event);
                break;
            case REJECT_EVENT:
                updateToReject(event);
                break;
            case SEND_TO_REVIEW:
                updateToPending(event);
                break;
        }
    }

    private void updateToCancel(Event event) {
        event.setState(findEventStateOrThrow(EventStates.CANCELED));
    }

    private void updateToPublish(Event event) {
        if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1))
                && event.getState().getId() == 1) {
            event.setState(findEventStateOrThrow(EventStates.PUBLISHED));
            event.setPublishedOn(LocalDateTime.now());
        } else if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new OperationFailedException(
                    "Too late to publish event (less than 1 hour)"
            );
        } else if (event.getState().getId() != 1) {
            throw new OperationFailedException(
                    "Can't publish not pending event"
            );
        }
    }

    public void updateToReject(Event event) {
        if (!event.getState().getName().equals(EventStates.PUBLISHED.name())) {
            event.setState(findEventStateOrThrow(EventStates.CANCELED));
        } else {
            throw new OperationFailedException(
                    "Can't reject published event"
            );
        }
    }

    public void updateToPending(Event event) {
        event.setState(findEventStateOrThrow(EventStates.PENDING));
    }

    private void updateLocation(Event event, EventRequestDto dto) {
        Location location = findLocationOrSave(LocationMapper.toLocation(dto));
        event.setLocation(location);
    }
    //END MODIFY OBJ

    //GET OBJECTS
    public Location findLocationOrSave(Location location) {
        return locationRepository.find(location.getLat(), location.getLon())
                .orElseGet(() -> locationRepository.save(location));
    }

    public EventState findEventStateOrThrow(EventStates state) {
        return eventStateRepository.findByName(state.toString())
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Status name = %s not exists", state))
                );
    }

    public Event findEventOrThrow(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Event id = %s not exists", eventId))
                );
    }

    public Set<Event> findEventsByIds(List<Long> ids) {
        return eventRepository.findByIdIn(ids);
    }

    public User findUserOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("User id = %s was not found", userId))
                );
    }

    public EventRequestStatus findRequestStatusOrThrow(EventRequestStatuses status) {
        return eventRequestStatusRepository.findByName(status.name())
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Status %s not exists", status))
                );
    }

    public Event findPublicEventOrThrow(long eventId) {
        return eventRepository.findPublicById(eventId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Event id = %s not exists", eventId))
                );
    }

    public EventRequest findEventRequestOrThrow(long requestId) {
        return eventRequestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Event request с id = %s not exists", requestId))
                );
    }

    public Compilation findCompilationOrThrow(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Compilation id = %s not exists", compId))
                );
    }

    public List<EventRequest> findConfirmedRequests(Event event) {
        return eventRequestRepository.findConfirmedRequests(event.getId());
    }

    public Map<Event, List<EventRequest>> findConfirmedRequests(List<Event> events) {
        List<EventRequest> confirmedRequests = eventRequestRepository.findConfirmedRequests(
                events.stream().map(Event::getId).collect(Collectors.toList())
        );

        return confirmedRequests.stream()
                .collect(Collectors.groupingBy(EventRequest::getEvent, Collectors.toList()));
    }

    public List<Event> findByFilter(Pageable pageable, EventQueryFilter filter) {
        Predicate main = makePredicate(filter);
        Predicate text = makeTextPredicate(filter);
        Predicate available = makeIsAvailablePredicate(filter);

        Predicate predicate = QueryPredicates.builder()
                .add(main)
                .add(text)
                .add(available)
                .buildAnd();
        if (predicate != null) {
            return eventRepository.findAll(predicate, pageable).toList();
        } else {
            return eventRepository.findAll(pageable).toList();
        }

    }

    public Map<Event, List<EventRequest>> findConfirmedRequestsByCompilations(List<Compilation> compilations) {
        Set<Event> events = new HashSet<>();
        compilations.forEach(c -> events.addAll(c.getEvents()));

        return findConfirmedRequests(new ArrayList<>(events));
    }
    //END GET OBJECTS

    //STATS
    public Map<Long, Integer> findEventsViews(List<Event> events) {
        Optional<LocalDateTime> start = events.stream().map(Event::getCreatedOn).filter(Objects::nonNull)
                .min(Comparator.naturalOrder());
        return toStatsMap(
                statsClient.getStat(start.orElse(LocalDateTime.now()).minusSeconds(1),
                        LocalDateTime.now().plusSeconds(1), fillUris(events), true)
        );
    }

    public Map<Long, Integer> findEventViews(Event event) {
        LocalDateTime start = event.getCreatedOn();
        return toStatsMap(
                statsClient.getStat(start.minusSeconds(1), LocalDateTime.now().plusSeconds(1),
                        fillUris(List.of(event)), true)
        );
    }

    public static Map<Long, Integer> toStatsMap(List<StatDto> stats) {
        return stats.stream()
                .collect(
                        toMap(stat -> Long.valueOf(stat.getUri().substring(8)), StatDto::getHits)
                );
    }

    private List<String> fillUris(List<Event> events) {
        if (events.isEmpty()) {
            return List.of("/events/");
        } else {
            return events.stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList());
        }
    }
    //END STATS

    //PAGEABLE
    public Pageable createPageable(String sort, int from, int size) {
        return PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.ASC, sort)
        );
    }

    public Pageable createPageableBySort(EventSortBy sort, int from, int size) {
        if (sort.equals(EventSortBy.VIEWS)) {
            return createPageable("views", from, size);
        } else {
            return createPageable("eventDate", from, size);
        }
    }
    //END PAGEABLE

    //PREDICATES
    private Predicate makePredicate(EventQueryFilter filter) {
        return QueryPredicates.builder()
                .add(filter.getUserIds(), event.initiator.id::in)
                .add(filter.getStates(), event.state.name::in)
                .add(filter.getCategories(), event.category.id::in)
                .add(filter.getPaid(), event.paid::eq)
                .add(filter.getRangeStart(), event.eventDate::after)
                .add(filter.getRangeEnd(), event.eventDate::before)
                .buildAnd();
    }

    private Predicate makeTextPredicate(EventQueryFilter filter) {
        if (filter.getText() == null || filter.getText().isBlank()) return null;
        return QueryPredicates.builder()
                .add(event.annotation.likeIgnoreCase(filter.getText()))
                .add(event.description.likeIgnoreCase(filter.getText()))
                .buildOr();
    }

    private Predicate makeIsAvailablePredicate(EventQueryFilter filter) {
        if (filter.isOnlyAvailable()) {
            return event.state.id.in(2);
        } else return null;
    }
    //END PREDICATE
}
