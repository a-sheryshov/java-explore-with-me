package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
