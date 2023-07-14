package ru.practicum.ewm.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.event.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location as l where " +
            "l.lat = :lat and l.lon = :lon")
    Optional<Location> find(double lat, double lon);
}
