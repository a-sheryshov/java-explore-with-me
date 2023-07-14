package ru.practicum.ewm.mainservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class LocationDto {
    private double lat;
    private double lon;
}
