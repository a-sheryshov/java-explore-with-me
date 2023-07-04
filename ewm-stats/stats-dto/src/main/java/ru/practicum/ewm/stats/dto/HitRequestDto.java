package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class HitRequestDto {
    @NotBlank
    @Size(max = 50, message = "Should be less than 50 symbols")
    private final String app;
    @NotBlank
    @Size(max = 100, message = "Should be less than 100 symbols")
    private final String uri;
    @NotBlank
    @Size(max = 50, message = "Should be less than 50 symbols")
    private final String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("timestamp")
    private final LocalDateTime timeStamp;
}