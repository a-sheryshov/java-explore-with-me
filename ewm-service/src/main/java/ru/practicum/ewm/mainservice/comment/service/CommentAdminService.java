package ru.practicum.ewm.mainservice.comment.service;

import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentStateActionRequestDto;

public interface CommentAdminService {

    CommentResponseDto updateCommentByAdmin(Long commentId,
                                            CommentStateActionRequestDto dto);

    CommentResponseDto getCommentById(Long commentId);
}