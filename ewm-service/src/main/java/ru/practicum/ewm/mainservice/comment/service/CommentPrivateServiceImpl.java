package ru.practicum.ewm.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.comment.dto.CommentCreateRequestDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentUpdateRequestDto;
import ru.practicum.ewm.mainservice.comment.mapper.CommentMapper;
import ru.practicum.ewm.mainservice.comment.model.Comment;
import ru.practicum.ewm.mainservice.comment.model.CommentState;
import ru.practicum.ewm.mainservice.comment.repository.CommentRepository;
import ru.practicum.ewm.mainservice.event.model.Event;
import ru.practicum.ewm.mainservice.event.model.EventStates;
import ru.practicum.ewm.mainservice.exception.ObjectNotExistsException;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;
import ru.practicum.ewm.mainservice.user.model.User;
import ru.practicum.ewm.mainservice.util.CommonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommonService commonService;

    @Override
    public void deleteComment(Long commentId, Long userId) {
        commonService.findUserOrThrow(userId);
        Comment comment = getCommentIfExists(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new OperationFailedException("Operation denied");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentResponseDto createComment(CommentCreateRequestDto dto, Long userId) {
        User user = commonService.findUserOrThrow(userId);
        Event event = commonService.findEventOrThrow(dto.getEventId());
        if (!Objects.equals(event.getState().getName(), EventStates.PUBLISHED.name())) {
            throw new OperationFailedException("Event is not published");
        }
        Comment comment = commentMapper.toComment(dto);
        comment.setAuthor(user)
                .setEvent(event)
                .setCreated(LocalDateTime.now())
                .setCommentState(CommentState.PENDING);
        CommentResponseDto dtoResponse = commentMapper.toDto(commentRepository.save(comment));
        log.info("Comment id = {} was created", comment.getId());
        return dtoResponse;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentResponseDto updateComment(CommentUpdateRequestDto dto, Long commentId, Long userId) {
        commonService.findUserOrThrow(userId);
        Comment comment = getCommentIfExists(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new OperationFailedException("Operation denied");
        }
        commentMapper.partialUpdate(dto, comment);
        comment.setCommentState(CommentState.PENDING);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByAuthorId(Long authorId, int from, int size) {
        commonService.findUserOrThrow(authorId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findCommentsByAuthorId(authorId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    private Comment getCommentIfExists(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ObjectNotExistsException(String.format("Comment id = %d not exists", commentId)));
    }

}
