package ru.practicum.ewm.mainservice.category.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Getter
public class CategoryDto {
    private final Long id;
    @NotBlank
    @Size(max = 50, message = "Should be less than 50 symbols")
    private final String name;
}
