package ru.practicum.ewm.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.comment.dto.CommentResponseDto;
import ru.practicum.ewm.mainservice.comment.service.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CommentPublicController {

    private final CommentPublicService commentService;

    @GetMapping("{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentsByEventId(@PathVariable Long eventId,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10", name = "size") Integer size) {
        return commentService.getCommentsByEventId(eventId, from, size);
    }
}