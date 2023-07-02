package ru.practicum.ewm.stats.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.service.model.Hit;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitStatRepository extends JpaRepository<Hit, Long> {
    @Query(name = "GetNotUniqueIpStat", nativeQuery = true)
    List<StatDto> getNotUniqueIpStat(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(name = "GetUniqueIpStat", nativeQuery = true)
    List<StatDto> getUniqueIpStat(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(name = "GetNotUniqueIpStatNoUri", nativeQuery = true)
    List<StatDto> getNotUniqueIpStatNoUri(LocalDateTime start, LocalDateTime end);

    @Query(name = "GetNotUniqueIpStatNoUri", nativeQuery = true)
    List<StatDto> getUniqueIpStatNoUri(LocalDateTime start, LocalDateTime end);
}
