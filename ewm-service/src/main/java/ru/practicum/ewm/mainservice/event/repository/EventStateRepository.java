package ru.practicum.ewm.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.mainservice.event.model.EventState;

import java.util.Optional;

public interface EventStateRepository extends JpaRepository<EventState, Long> {
    Optional<EventState> findByName(String state);
}
