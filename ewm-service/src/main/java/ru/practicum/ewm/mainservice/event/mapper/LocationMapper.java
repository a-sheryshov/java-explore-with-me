package ru.practicum.ewm.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainservice.event.dto.EventCreateRequestDto;
import ru.practicum.ewm.mainservice.event.dto.EventRequestDto;
import ru.practicum.ewm.mainservice.event.dto.LocationDto;
import ru.practicum.ewm.mainservice.event.model.Location;

@UtilityClass
public class LocationMapper {
    public static Location toLocation(EventRequestDto dto) {
        Location location = new Location();

        location.setLat(dto.getLocation().getLat());
        location.setLon(dto.getLocation().getLon());

        return location;
    }

    public static Location toLocation(EventCreateRequestDto dto) {
        Location location = new Location();

        location.setLat(dto.getLocation().getLat());
        location.setLon(dto.getLocation().getLon());

        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
