package ru.practicum.ewm.mainservice.comment.service;

import ru.practicum.ewm.mainservice.comment.dto.CommentCreateRequestDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentUpdateRequestDto;

import java.util.List;

public interface CommentPrivateService {

    CommentResponseDto createComment(CommentCreateRequestDto commentDtoCreate, Long userId);

    CommentResponseDto updateComment(CommentUpdateRequestDto commentDtoUpdate, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);

    List<CommentResponseDto> getCommentsByAuthorId(Long authorId, int from, int size);

}
