package ru.practicum.ewm.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.category.mapper.CategoryMapper;
import ru.practicum.ewm.mainservice.category.model.Category;
import ru.practicum.ewm.mainservice.event.dto.EventCreateRequestDto;
import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.model.Location;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.user.mapper.UserMapper;
import ru.practicum.ewm.mainservice.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event toEvent(EventCreateRequestDto dto, Category category, Location location, User user) {
        Event event = new Event();

        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());

        return event;
    }

    public static EventDto toEventDto(Event event,
                                      List<EventRequest> confirmedRequests,
                                      Map<Long, Integer> views) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState().getName())
                .title(event.getTitle())
                .views(views.getOrDefault(event.getId(), 0))
                .build();
    }

    public static List<EventDto> toEventDto(List<Event> events,
                                                    Map<Event, List<EventRequest>> confirmedRequests,
                                                    Map<Long, Integer> views) {
        return events.stream()
                .map(event -> toEventDto(
                        event,
                        confirmedRequests.getOrDefault(event, List.of()),
                        views
                ))
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShortDto(Event event,
                                                List<EventRequest> confirmedRequests,
                                                Map<Long, Integer> views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views.getOrDefault(event.getId(), 0))
                .build();
    }

    public static List<EventShortDto> toEventShortDto(List<Event> events,
                                                      Map<Event, List<EventRequest>> confirmedRequests,
                                                      Map<Long, Integer> views) {
        return events.stream()
                .map(event -> toEventShortDto(
                        event,
                        confirmedRequests.getOrDefault(event, List.of()),
                        views
                ))
                .collect(Collectors.toList());
    }
}
