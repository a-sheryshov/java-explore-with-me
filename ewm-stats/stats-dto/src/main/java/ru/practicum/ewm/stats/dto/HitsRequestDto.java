package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class HitsRequestDto {
    @NotBlank
    @Size(max = 50, message = "Should be less than 50 symbols")
    private final String app;
    @NotEmpty
    private final List<@Size(max = 100, message = "Should be less than 100 symbols") String> uris;
    @NotBlank
    @Size(max = 50, message = "Should be less than 50 symbols")
    private final String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("timestamp")
    private final LocalDateTime timeStamp;
}
