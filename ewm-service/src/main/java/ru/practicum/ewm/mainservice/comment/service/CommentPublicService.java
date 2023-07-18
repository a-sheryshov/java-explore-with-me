package ru.practicum.ewm.mainservice.comment.service;

import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentPublicService {
    List<CommentResponseDto> getCommentsByEventId(Long eventId, int from, int size);

}
