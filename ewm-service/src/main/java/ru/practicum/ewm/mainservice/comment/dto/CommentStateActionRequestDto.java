package ru.practicum.ewm.mainservice.comment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.mainservice.comment.model.CommentStateAction;

@Getter
@Setter
@Builder
@Jacksonized
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentStateActionRequestDto {
    private CommentStateAction commentState;
}
