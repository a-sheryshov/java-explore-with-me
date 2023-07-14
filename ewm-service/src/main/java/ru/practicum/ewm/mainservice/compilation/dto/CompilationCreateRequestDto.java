package ru.practicum.ewm.mainservice.compilation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CompilationCreateRequestDto {
    @JsonProperty("events")
    private final List<Long> eventIds;
    private final Boolean pinned = false;
    @NotBlank
    @Size(min = 3, max = 50, message = "Invalid title length")
    private final String title;
}
