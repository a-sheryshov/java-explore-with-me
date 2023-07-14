package ru.practicum.ewm.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.event.dto.*;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.event.service.EventPrivateService;
import ru.practicum.ewm.mainservice.util.validation.CreateValidationGroup;
import ru.practicum.ewm.mainservice.util.validation.UpdateValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class EventPrivateController {
    private final EventPrivateService eventService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventDto create(@Validated(CreateValidationGroup.class) @Valid @RequestBody EventCreateRequestDto dto,
                           @PathVariable("userId") @Positive long userId) {
        return eventService.create(dto, userId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventDto update(@Validated(UpdateValidationGroup.class) @Valid @RequestBody EventRequestDto dto,
                               @PathVariable("userId") @Positive long userId,
                               @PathVariable("eventId") @Positive long eventId) {
        return eventService.update(dto, userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable("userId") @Positive long userId,
                                          @RequestParam(name = "from", defaultValue = "0")
                                          @PositiveOrZero int from,
                                          @RequestParam(name = "size", defaultValue = "10")
                                          @Positive int size) {
        return eventService.getAll(userId, from, size);
    }

    @GetMapping(path = "/{eventId}")
    public EventDto getBuId(@PathVariable("userId") @Positive long userId,
                                @PathVariable("eventId") @Positive long eventId) {
        return eventService.getById(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<EventReqRequestDto> getRequests(@PathVariable("userId") @Positive long userId,
                                                @PathVariable("eventId") @Positive long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public ProcessReqResponseDto processRequests(@PathVariable("userId") @Positive long userId,
                                                 @PathVariable("eventId") @Positive long eventId,
                                                 @Valid @RequestBody ProcessReqRequestDto dto) {
        return eventService.processRequests(userId, eventId, dto);
    }
}
