package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import ru.practicum.ewm.mainservice.util.validation.UpdateValidationGroup;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequestDto {
    @Size(min = 20, max = 2000, message = "Invalid annotation length")
    private final String annotation;
    @Positive(groups = {UpdateValidationGroup.class})
    @JsonProperty("category")
    private final Long categoryId;
    @Size(min = 20, max = 7000, message = "Invalid description length")
    private final String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("eventDate")
    private final LocalDateTime eventDate;
    private final LocationDto location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private final StateAction stateAction;
    @Size(min = 3, max = 120, message = "Invalid title length")
    private final String title;

}
