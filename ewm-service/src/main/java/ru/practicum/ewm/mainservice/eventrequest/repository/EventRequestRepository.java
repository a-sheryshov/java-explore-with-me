package ru.practicum.ewm.mainservice.eventrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.eventrequest.model.EventRequest;
import ru.practicum.ewm.mainservice.user.model.User;


import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    @Query("select count(e) from EventRequest as e " +
            "where e.requester.id = :userId and e.event.id = :eventId")
    int checkRequest(long userId, long eventId);

    List<EventRequest> findAllByRequester(User requester);

    List<EventRequest> findAllByEvent(Event event);

    List<EventRequest> findAllByIdIn(List<Long> requestIds);

    @Query("select r from EventRequest as r " +
            "where r.status.id = 2 and r.event.id = :id ")
    List<EventRequest> findConfirmedRequests(Long id);

    @Query("select r from EventRequest as r " +
            "where r.status.id = 2 and r.event.id in :ids ")
    List<EventRequest> findConfirmedRequests(List<Long> ids);
}
