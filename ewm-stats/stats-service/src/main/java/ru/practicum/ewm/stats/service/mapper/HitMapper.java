package ru.practicum.ewm.stats.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.dto.HitsRequestDto;
import ru.practicum.ewm.stats.service.model.App;
import ru.practicum.ewm.stats.service.model.Hit;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class HitMapper {
    public static Hit toHitList(HitRequestDto dto, App app) {
        Hit hit = new Hit();
        hit.setApp(app);
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimeStamp(dto.getTimeStamp());
        return hit;
    }

    public static List<Hit> toHitList(HitsRequestDto dto, App app) {
        return dto.getUris().stream()
                .map(uri -> {
                    Hit hit = new Hit();
                    hit.setApp(app);
                    hit.setUri(uri);
                    hit.setIp(dto.getIp());
                    hit.setTimeStamp(dto.getTimeStamp());
                    return hit;
                })
                .collect(Collectors.toList());
    }

    public static HitResponseDto toResponseDto(Hit hit) {
        return HitResponseDto.builder()
                .id(hit.getId())
                .app(hit.getApp().getName())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timeStamp(hit.getTimeStamp())
                .build();
    }
}