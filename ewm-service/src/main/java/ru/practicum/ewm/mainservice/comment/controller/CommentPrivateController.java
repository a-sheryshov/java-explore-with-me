package ru.practicum.ewm.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.comment.dto.CommentCreateRequestDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentUpdateRequestDto;
import ru.practicum.ewm.mainservice.comment.service.CommentPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentPrivateService commentService;

    @PostMapping("comments/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable Long userId,
                                         @Valid @RequestBody CommentCreateRequestDto commentDtoCreate) {
        return commentService.createComment(commentDtoCreate, userId);
    }

    @PatchMapping("comments/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@PathVariable Long userId,
                                            @PathVariable Long commentId,
                                            @Valid @RequestBody CommentUpdateRequestDto commentDtoUpdate) {
        return commentService.updateComment(commentDtoUpdate, commentId, userId);
    }

    @GetMapping("comments/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentByAuthorId(@PathVariable Long userId,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10", name = "size") Integer size) {
        return commentService.getCommentsByAuthorId(userId, from, size);
    }

    @DeleteMapping("comments/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(commentId, userId);
    }

}
