package ru.practicum.ewm.mainservice.eventrequest.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_requests")
@Setter
@Getter
public class EventRequest {
    @Id
    @Column(name = "event_requests_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id")
    private User requester;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private EventRequestStatus status;

}