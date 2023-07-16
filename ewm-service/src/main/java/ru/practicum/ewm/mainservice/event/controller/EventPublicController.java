package ru.practicum.ewm.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.event.dto.EventDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.dto.EventSortBy;
import ru.practicum.ewm.mainservice.event.service.EventPublicService;
import ru.practicum.ewm.mainservice.event.service.util.PublicServiceGetAllParameter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class EventPublicController {
    private final EventPublicService eventPublicService;
    private final HttpServletRequest request;

    @GetMapping
    public List<EventShortDto> findByFilter(@RequestParam(name = "text", defaultValue = "") String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false)
                                            LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false)
                                            LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                            boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") EventSortBy sort,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return eventPublicService.getAll(PublicServiceGetAllParameter.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort).from(from).size(size).ip(request.getRemoteAddr())
                .build());
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable("id") @Positive long eventId) {
        return eventPublicService.getById(eventId, request.getRemoteAddr());
    }
}
