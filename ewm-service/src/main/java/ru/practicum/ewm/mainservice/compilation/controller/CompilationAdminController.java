package ru.practicum.ewm.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationCreateRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationRequestDto;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationResponseDto create(@Valid @RequestBody CompilationCreateRequestDto dto) {
        return compilationService.create(dto);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationResponseDto update(@Valid @RequestBody CompilationRequestDto dto,
                                         @PathVariable("compId") long compId) {
        return compilationService.update(dto, compId);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("compId") @Positive long compId) {
        compilationService.delete(compId);
    }
}
