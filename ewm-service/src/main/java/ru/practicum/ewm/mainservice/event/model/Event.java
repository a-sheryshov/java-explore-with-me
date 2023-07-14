package ru.practicum.ewm.mainservice.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.category.model.Category;
import ru.practicum.ewm.mainservice.compilation.model.Compilation;
import ru.practicum.ewm.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Setter
@Getter
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "state_id")
    private EventState state;
    @Column(name = "title")
    private String title;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
}
