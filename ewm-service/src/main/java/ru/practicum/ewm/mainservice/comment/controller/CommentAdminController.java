package ru.practicum.ewm.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentStateActionRequestDto;
import ru.practicum.ewm.mainservice.comment.service.CommentAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable Long commentId) {
        return commentAdminService.getCommentById(commentId);
    }

    @PatchMapping("comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateCommentStatus(@PathVariable Long commentId,
                                                  @Valid @RequestBody CommentStateActionRequestDto adminRequest) {
        return commentAdminService.updateCommentByAdmin(commentId, adminRequest);
    }
}
