package ru.practicum.ewm.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationResponseDto> getAll(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationResponseDto getById(@PathVariable("compId") @Positive long compId) {
        return compilationService.getById(compId);
    }
}
