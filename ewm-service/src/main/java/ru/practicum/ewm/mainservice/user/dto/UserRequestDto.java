package ru.practicum.ewm.mainservice.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Builder(toBuilder = true)
@Getter
@RequiredArgsConstructor
public class UserRequestDto {
    @NotBlank
    @Size(min = 2, max = 250, message = "Invalid username length")
    private final String name;
    @Email
    @Size(min = 6, max = 254, message = "Invalid email length")
    @NotEmpty
    private final String email;
}