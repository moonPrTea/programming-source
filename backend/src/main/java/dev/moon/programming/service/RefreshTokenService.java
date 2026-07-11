package dev.moon.programming.service;

import dev.moon.programming.dto.auth.request.RefreshTokenRequest;
import dev.moon.programming.dto.auth.response.RefreshTokenResponse;
import dev.moon.programming.model.entity.UserEntity;
import dev.moon.programming.model.enums.TokenType;
import dev.moon.programming.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
  private final UserRepository userRepository;
  private final StringRedisTemplate redis;
  private final JwtService jwtService;
  private final Duration refreshTokenTtl;
  private final String refreshTokenCookie;

  public RefreshTokenService(UserRepository userRepository, StringRedisTemplate redis, JwtService jwtService,
                             @Value("${application.security.jwt.refresh-token.expiration}") Duration refreshTokenTtl,
                             @Value("${application.security.jwt.refresh-token.cookie-name}") String refreshTokenCookie) {
    this.userRepository = userRepository;
    this.redis = redis;
    this.jwtService = jwtService;
    this.refreshTokenTtl = refreshTokenTtl;
    this.refreshTokenCookie = refreshTokenCookie;
  }

  public String createRefreshToken(UUID userId) {
    String token = generateToken();

    redis.opsForValue().set(createKey(token), String.valueOf(userId), refreshTokenTtl);

    return token;
  }

  private String generateToken() {
    return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(UUID.randomUUID().toString().getBytes());
  }

  private String createKey(String token) {
    return "refresh_token:" + token;
  }

  public RefreshTokenResponse createNewToken(RefreshTokenRequest tokenRequest) throws Exception {
    String token = tokenRequest.refreshToken();
    UUID userId = Optional.ofNullable(redis.opsForValue().get(createKey(token)))
            .map(UUID::fromString)
            .orElseThrow(() -> new Exception("Refresh token expired or does not exist"));

    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Not found user"));

    String accessToken = jwtService.generateToken(user);

    return new RefreshTokenResponse(accessToken, token, TokenType.BEARER.name());
  }

  public ResponseCookie createRefreshTokenCookie(String token) {
    return ResponseCookie
            .from(refreshTokenCookie, token)
            .path("/")
            .maxAge(refreshTokenTtl)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .build();
  }

  public ResponseCookie getEmptyRefreshTokenCookie() {
    return ResponseCookie
            .from(refreshTokenCookie, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .build();
  }

  public String getRefreshTokenFromCookie(HttpServletRequest request) {
    Cookie cookie = WebUtils
            .getCookie(request, refreshTokenCookie);

    return cookie != null ? cookie.getValue() : null;
  }


  public void deleteRedisToken(String token) {
    redis.delete(createKey(token));
  }
}
