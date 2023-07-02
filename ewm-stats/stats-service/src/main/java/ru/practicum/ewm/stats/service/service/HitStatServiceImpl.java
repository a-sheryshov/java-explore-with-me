package ru.practicum.ewm.stats.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.service.exception.InvalidPeriodException;
import ru.practicum.ewm.stats.service.mapper.AppMapper;
import ru.practicum.ewm.stats.service.mapper.HitMapper;
import ru.practicum.ewm.stats.service.model.App;
import ru.practicum.ewm.stats.service.model.Hit;
import ru.practicum.ewm.stats.service.repository.AppRepository;
import ru.practicum.ewm.stats.service.repository.HitStatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class HitStatServiceImpl implements HitStatService {
    private final AppRepository appRepository;
    private final HitStatRepository hitStatRepository;

    @Override
    public HitResponseDto saveHit(HitRequestDto dto) {
        App app = getOrCreate(dto);

        Hit hit = hitStatRepository.save(HitMapper.toHit(dto, app));
        log.info("сохранен запрос ip = {} по url = {}", dto.getIp(), dto.getUri());

        return HitMapper.toResponseDto(hit);
    }

    @Override
    public List<StatDto> readStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        validatePeriod(start, end);
        return get(start, end, uris, unique);
    }

    private App getOrCreate(HitRequestDto dto) {
        return appRepository.findByName(dto.getApp())
                .orElseGet(() -> appRepository.save(AppMapper.toApp(dto)));
    }

    private List<StatDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris.stream().anyMatch(uri -> uri.equals("/events")) || uris.isEmpty()) {
            if (unique) {
                log.info("Query stats for no uris unique ip");
                return hitStatRepository.getUniqueIpStatNoUri(start, end);
            } else {
                log.info("Query stats for no uris not unique ip");
                return hitStatRepository.getNotUniqueIpStatNoUri(start, end);
            }
        } else {
            if (unique) {
                log.info("Query stats for uris ={} unique ip", uris);
                return hitStatRepository.getUniqueIpStat(start, end, uris);
            } else {
                log.info("Query stats for uris ={} not unique ip", uris);
                return hitStatRepository.getNotUniqueIpStat(start, end, uris);
            }
        }
    }

    private void validatePeriod(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new InvalidPeriodException("End time should be after start time");
        }
    }
}
