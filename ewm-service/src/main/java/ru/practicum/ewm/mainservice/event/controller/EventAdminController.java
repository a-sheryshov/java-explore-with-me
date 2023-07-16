package ru.practicum.ewm.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.model.EventStates;
import ru.practicum.ewm.mainservice.event.service.EventAdminService;
import ru.practicum.ewm.mainservice.event.service.util.AdminServiceGetAllParameter;
import ru.practicum.ewm.mainservice.util.validation.UpdateValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class EventAdminController {
    private final EventAdminService eventAdminServiceService;

    @PatchMapping(path = "/{eventId}")
    public EventDto update(@Validated(UpdateValidationGroup.class) @Valid @RequestBody EventRequestDto dto,
                           @PathVariable("eventId") @Positive long eventId) {
        return eventAdminServiceService.update(dto, eventId);
    }

    @GetMapping
    public List<EventDto> findByFilter(@RequestParam(name = "users", required = false) List<Long> userIds,
                                           @RequestParam(name = "states", required = false) List<EventStates> states,
                                           @RequestParam(name = "categories", required = false) List<Long> categories,
                                           @RequestParam(name = "rangeStart", required = false)
                                           LocalDateTime rangeStart,
                                           @RequestParam(name = "rangeEnd", required = false)
                                           LocalDateTime rangeEnd,
                                           @RequestParam(name = "from", defaultValue = "0")
                                           @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10")
                                           @Positive int size) {
        return eventAdminServiceService.getAll(AdminServiceGetAllParameter.builder()
                        .userIds(userIds)
                        .states(states)
                        .categories(categories)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .from(from)
                        .size(size)
                        .build());
    }
}
