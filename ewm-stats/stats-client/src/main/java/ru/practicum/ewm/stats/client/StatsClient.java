package ru.practicum.ewm.stats.client;

import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    void saveHit(HitRequestDto dto);

    List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
