package ru.practicum.ewm.mainservice.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationCreateRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.mainservice.compilation.model.Compilation;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(CompilationCreateRequestDto dto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());
        return compilation;
    }

    public static CompilationResponseDto toCompilationDto(Compilation compilation,
                                                          Map<Event, List<EventRequest>> confirmedRequests,
                                                          Map<Long, Integer> views) {
        return CompilationResponseDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(EventMapper.toEventShortDto(
                        new ArrayList<>(compilation.getEvents()),
                        confirmedRequests,
                        views
                ))
                .build();
    }

    public static List<CompilationResponseDto> toCompilationDto(List<Compilation> compilations,
                                                        Map<Event, List<EventRequest>> confirmedRequests,
                                                        Map<Long, Integer> views) {
        return compilations.stream()
                .map(compilation -> toCompilationDto(
                        compilation,
                        confirmedRequests,
                        views
                ))
                .collect(Collectors.toList());
    }
}
