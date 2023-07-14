package ru.practicum.ewm.stats.client;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitsRequestDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsClientImpl implements StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${stats-server.uri}")
    private String uri;


    @Override
    public void saveHit(HitRequestDto dto) {
        try {
            Unirest.post(uri + "/hit")
                    .body(dto)
                    .contentType("application/json")
                    .asEmpty();
            log.info("Hit POST request (ip = {}, uri = {}) was sent", dto.getIp(), dto.getUri());
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void saveHits(HitsRequestDto dto) {
        try {
            Unirest.post(uri + "/hits")
                    .body(dto)
                    .contentType("application/json")
                    .asEmpty();
            log.info("Hits POST request (ip = {}, uri.count = {}) was sent", dto.getIp(), dto.getUris().size());
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }
    }

    @Override
    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Stat filtered GET request (uris = {}) was sent", uris);
        return get(encodeTime(start), encodeTime(end), uris, unique);
    }

    private String encodeTime(LocalDateTime time) {
        return time.format(formatter);
    }

    private List<StatDto> get(String start, String end, List<String> uris, boolean unique) {
        return Unirest.get(uri + "/stats")
                .queryString("start", start)
                .queryString("end", end)
                .queryString("unique", unique)
                .queryString("uris", uris)
                .asObject(new GenericType<List<StatDto>>(){})
                .getBody();
    }

    private List<StatDto> get(List<String> uris) {
        return Unirest.get(uri + "/stats")
                .queryString("unique", true)
                .queryString("uris", uris)
                .queryString("start", LocalDateTime.now().minusYears(5))
                .queryString("end", LocalDateTime.now().plusYears(5))
                .asObject(new GenericType<List<StatDto>>(){})
                .getBody();
    }
}
