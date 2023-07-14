package ru.practicum.ewm.stats.service.service;

import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.dto.HitsRequestDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitStatService {
    HitResponseDto saveHit(HitRequestDto dto);

    void saveHits(HitsRequestDto dto);

    List<StatDto> readStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
