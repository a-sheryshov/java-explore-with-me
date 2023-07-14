package ru.practicum.ewm.mainservice.compilation.service;

import ru.practicum.ewm.mainservice.compilation.dto.CompilationCreateRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationResponseDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto create(CompilationCreateRequestDto dto);

    CompilationResponseDto update(CompilationRequestDto dto, long compId);

    void delete(long compId);

    List<CompilationResponseDto> getAll(Boolean pinned, int from, int size);

    CompilationResponseDto getById(long compId);
}
