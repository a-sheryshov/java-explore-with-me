package ru.practicum.ewm.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentStateActionRequestDto;
import ru.practicum.ewm.mainservice.comment.mapper.CommentMapper;
import ru.practicum.ewm.mainservice.comment.model.Comment;
import ru.practicum.ewm.mainservice.comment.model.CommentState;
import ru.practicum.ewm.mainservice.comment.model.CommentStateAction;
import ru.practicum.ewm.mainservice.comment.repository.CommentRepository;
import ru.practicum.ewm.mainservice.exception.ObjectNotExistsException;

import java.time.LocalDateTime;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentResponseDto updateCommentByAdmin(Long commentId,
                                                   CommentStateActionRequestDto updateRequest) {
        Comment comment = findCommentById(commentId);

        if (updateRequest.getCommentState().equals(CommentStateAction.PUBLISH)) {
            comment.setCommentState(CommentState.PUBLISHED);
            comment.setPublished(LocalDateTime.now());
        } else {
            comment.setCommentState(CommentState.REJECTED);
        }
        log.info("Comment's id = {} state was updated by admin", commentId);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentResponseDto getCommentById(Long commentId) {
        return commentMapper.toDto(findCommentById(commentId));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new ObjectNotExistsException(String.format("Comment id = %d not exists", commentId)));
    }
}
