package dev.moon.programming.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record UserRoleResponse(
        String username,
        String email,
        String role,
        String description,
        @JsonProperty("birth_date")
        LocalDate birthDate
) {
}
