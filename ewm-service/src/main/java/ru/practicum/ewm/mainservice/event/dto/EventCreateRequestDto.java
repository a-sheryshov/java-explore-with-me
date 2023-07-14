package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.mainservice.util.validation.CreateValidationGroup;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCreateRequestDto {
    @NotBlank(groups = CreateValidationGroup.class)
    @Size(min = 20, max = 2000, message = "Invalid annotation length")
    private final String annotation;
    @Positive(groups = CreateValidationGroup.class)
    @NotNull(groups = CreateValidationGroup.class)
    @JsonProperty("category")
    private final Long categoryId;
    @NotBlank(groups = {CreateValidationGroup.class})
    @Size(min = 20, max = 7000, message = "Invalid description length")
    private final String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("eventDate")
    private final LocalDateTime eventDate;
    @NotNull(groups = CreateValidationGroup.class)
    private final LocationDto location;
    private final Boolean paid = false;
    private final Integer participantLimit = 0;
    private final Boolean requestModeration = true;
    private final StateAction stateAction;
    @NotBlank(groups = CreateValidationGroup.class)
    @Size(min = 3, max = 120, message = "Invalid title length")
    private final String title;

}