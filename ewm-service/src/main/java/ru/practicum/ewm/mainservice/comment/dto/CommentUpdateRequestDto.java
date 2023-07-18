package ru.practicum.ewm.mainservice.comment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Jacksonized
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {
    @NotNull(message = "Should be presented")
    @Size(min = 3, max = 500)
    private String text;
}