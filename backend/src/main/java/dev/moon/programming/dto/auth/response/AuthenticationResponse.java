package dev.moon.programming.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AuthenticationResponse(
        UUID id,
        String email,
        List<String> roles,
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("token_type")
        String tokenType
) {
}
