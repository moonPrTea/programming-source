package dev.moon.programming.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegistrationRequest(
        @JsonProperty("first_name")
        @NotNull(message = "first_name is a required field")
        @NotBlank(message = "first_name can't be blank")
        String firstName,
        @JsonProperty("last_name")
        @NotNull(message = "last_name is a required field")
        @NotBlank(message = "last_name can't be blank")
        String lastName,
        @JsonProperty("middle_name")
        String middleName,
        @NotNull(message = "username is a required field")
        @NotBlank(message = "username can't be blank")
        String username,
        @NotNull(message = "email is a required field")
        @NotBlank(message = "email can't be blank")
        String email,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        @NotNull(message = "email is a required field")
        @NotBlank(message = "email is can't be blank")
        String password
) {
}
