package ru.practicum.ewm.stats.client;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.StatDto;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsClientImpl implements StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${stats-client.uri}")
    private final String uri;

    @Override
    public void saveHit(HitRequestDto dto) {
        Unirest.post(uri + "/hit").body(dto);
        log.info("Hit POST request (ip = {}, url = {}) was sent", dto.getIp(), dto.getUri());
    }

    @Override
    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Stat GET request (uris = {}) was sent", uris);
        return get(encodeTime(start), encodeTime(end), uris, unique);
    }

    private String encodeTime(LocalDateTime time) {
        String str = time.format(formatter);
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    private List<StatDto> get(String start, String end, List<String> uris, boolean unique) {
        return Unirest.get(uri + "/stats")
                .queryString("start", start)
                .queryString("end", end)
                .queryString("uris", uris)
                .queryString("unique", unique)
                .asObject(new GenericType<List<StatDto>>(){})
                .getBody();
    }
}
