package ru.practicum.ewm.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.comment.model.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c from Comment c " +
            "WHERE c.event.id = :eventId " +
            "and c.commentState = 'PUBLISHED'")
    List<Comment> findCommentsByEventId(Long eventId, Pageable pageable);

    @Query("SELECT c from Comment c " +
            "WHERE c.author.id = :authorId " +
            "and c.commentState = 'PUBLISHED'")
    List<Comment> findCommentsByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT c from Comment c " +
            "WHERE c.author.id = :authorId " +
            "and c.id = :commentId")
    Optional<Comment> findCommentByAuthorIdAndId(Long authorId, Long commentId);
}