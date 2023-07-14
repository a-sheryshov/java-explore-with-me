package ru.practicum.ewm.mainservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.mainservice.category.model.Category;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    @Query("select e from Event as e " +
            "where e.id = :eventId and e.state.id = 2")
    Optional<Event> findPublicById(long eventId);

    Set<Event> findByIdIn(List<Long> ids);

    List<Event> findAllByCategory(Category category);
}
