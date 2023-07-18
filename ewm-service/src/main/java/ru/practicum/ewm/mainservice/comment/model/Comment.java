package ru.practicum.ewm.mainservice.comment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User author;
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "comment_state")
    private CommentState commentState;
    @Column(name = "published")
    private LocalDateTime published;
}
