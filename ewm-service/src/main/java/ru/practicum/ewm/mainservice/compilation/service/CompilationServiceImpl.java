package ru.practicum.ewm.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationCreateRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.mainservice.compilation.model.Compilation;
import ru.practicum.ewm.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.util.CommonService;

import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationServiceImpl implements CompilationService {
    private final CommonService commonService;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationResponseDto create(CompilationCreateRequestDto dto) {
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;
        Compilation compilation = CompilationMapper.toCompilation(dto);
        if (dto.getEventIds() != null) {
            compilation.setEvents(commonService.findEventsByIds(dto.getEventIds()));
        } else {
            compilation.setEvents(new HashSet<>());
        }
        compilation = compilationRepository.save(compilation);
        confirmedRequests = commonService.findConfirmedRequests(
                new ArrayList<>(compilation.getEvents())
        );
        views = getViews(compilation);
        log.info("Compilation id = {} was created", compilation.getId());

        return CompilationMapper.toCompilationDto(compilation, confirmedRequests, views);
    }

    @Override
    @Transactional
    public CompilationResponseDto update(CompilationRequestDto dto, long compId) {
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;
        Compilation compilation = commonService.findCompilationOrThrow(compId);

        if (dto.getPinned() != null) compilation.setPinned(dto.getPinned());
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) compilation.setTitle(dto.getTitle());
        if (dto.getEventIds() != null && !dto.getEventIds().isEmpty()) {
            compilation.setEvents(commonService.findEventsByIds(dto.getEventIds()));
        }

        confirmedRequests = commonService.findConfirmedRequests(
                new ArrayList<>(compilation.getEvents())
        );
        views = getViews(compilation);
        log.info("Compilation id = {} was updated", compilation.getId());

        return CompilationMapper.toCompilationDto(compilation, confirmedRequests, views);
    }

    @Override
    @Transactional
    public void delete(long compId) {
        Compilation compilation = commonService.findCompilationOrThrow(compId);
        compilationRepository.delete(compilation);
        log.info("Compilation id = {} was deleted", compilation.getId());
    }

    @Override
    public List<CompilationResponseDto> getAll(Boolean pinned, int from, int size) {
        Set<Event> events = new HashSet<>();
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;
        List<Compilation> compilations;
        Pageable pageable = commonService.createPageable("id", from, size);

        if (pinned != null) {
            compilations = findAllByPinned(pinned, pageable);
        } else {
            compilations = findAll(pageable);
        }

        compilations.forEach(compilation -> events.addAll(compilation.getEvents()));
        confirmedRequests = commonService.findConfirmedRequestsByCompilations(compilations);
        views = commonService.findEventsViews(List.copyOf(events));

        return CompilationMapper.toCompilationDto(compilations, confirmedRequests, views);
    }

    @Override
    public CompilationResponseDto getById(long compId) {
        Map<Long, Integer> views;
        Map<Event, List<EventRequest>> confirmedRequests;

        Compilation compilation = commonService.findCompilationOrThrow(compId);
        confirmedRequests = commonService.findConfirmedRequests(
                new ArrayList<>(compilation.getEvents())
        );
        views = getViews(compilation);

        return CompilationMapper.toCompilationDto(compilation, confirmedRequests, views);
    }

    private List<Compilation> findAllByPinned(boolean pinned, Pageable pageable) {
        return compilationRepository.findAllByPinned(pinned, pageable).toList();
    }

    private List<Compilation> findAll(Pageable pageable) {
        return compilationRepository.findAll(pageable).toList();
    }

    private Map<Long, Integer> getViews(Compilation compilation) {
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            return commonService.findEventsViews(List.copyOf(compilation.getEvents()));
        } else {
            return Map.of();
        }
    }
}