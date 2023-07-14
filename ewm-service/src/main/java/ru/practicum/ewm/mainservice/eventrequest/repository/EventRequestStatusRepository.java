package ru.practicum.ewm.mainservice.eventrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequestStatus;


import java.util.Optional;

public interface EventRequestStatusRepository extends JpaRepository<EventRequestStatus, Long> {
    Optional<EventRequestStatus> findByName(String name);
}
