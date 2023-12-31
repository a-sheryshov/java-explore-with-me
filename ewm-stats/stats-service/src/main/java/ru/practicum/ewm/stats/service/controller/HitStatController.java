package ru.practicum.ewm.stats.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.dto.HitsRequestDto;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.service.service.HitStatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class HitStatController {
    private final HitStatService hitStatService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HitResponseDto saveHit(@Valid @RequestBody HitRequestDto dto) {
        return hitStatService.saveHit(dto);
    }

    @PostMapping("/hits")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveHits(@Valid @RequestBody HitsRequestDto dto) {
        hitStatService.saveHits(dto);
    }

    @GetMapping("/stats")
    public List<StatDto> readStats(@RequestParam
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",
                                               fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS]"})
                                       LocalDateTime start,
                                   @RequestParam
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",
                                               fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS]"})
                                       LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        return hitStatService.readStats(start, end, uris, unique);
    }
}
