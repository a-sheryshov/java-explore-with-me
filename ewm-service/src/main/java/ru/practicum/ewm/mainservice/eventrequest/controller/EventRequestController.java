package ru.practicum.ewm.mainservice.eventrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.eventrequest.dto.EventReqRequestDto;
import ru.practicum.ewm.mainservice.eventrequest.service.EventRequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class EventRequestController {
    private final EventRequestService eventRequestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventReqRequestDto create(@PathVariable("userId") @Positive long userId,
                                     @RequestParam(name = "eventId") @Positive long eventId) {
        return eventRequestService.create(userId, eventId);
    }

    @GetMapping
    public List<EventReqRequestDto> getAll(@PathVariable("userId") @Positive long userId) {
        return eventRequestService.getAll(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public EventReqRequestDto cancel(@PathVariable("userId") @Positive long userId,
                                  @PathVariable(name = "requestId") @Positive long requestId) {
        return eventRequestService.cancel(userId, requestId);
    }
}