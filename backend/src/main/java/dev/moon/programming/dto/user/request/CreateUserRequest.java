package dev.moon.programming.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record CreateUserRequest(
        String username,
        String email,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        String role,
        String description
) {
}
