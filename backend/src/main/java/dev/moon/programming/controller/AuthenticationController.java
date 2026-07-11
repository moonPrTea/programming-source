package dev.moon.programming.controller;

import dev.moon.programming.dto.auth.request.AuthenticationRequest;
import dev.moon.programming.dto.auth.request.RegistrationRequest;
import dev.moon.programming.dto.auth.response.AuthenticationResponse;
import dev.moon.programming.service.AuthenticationService;
import dev.moon.programming.service.JwtService;
import dev.moon.programming.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationService = authenticationService;
    this.refreshTokenService = refreshTokenService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
    AuthenticationResponse response = authenticationService
            .register(request);

    return createCookieResponse(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = authenticationService
            .authenticate(request);

    return createCookieResponse(response);
  }

  private ResponseEntity<AuthenticationResponse> createCookieResponse(AuthenticationResponse response) {
    ResponseCookie jwtCookie = jwtService.createJwtCookie(response.accessToken());
    ResponseCookie refreshCookie = refreshTokenService.createRefreshTokenCookie(response.refreshToken());

    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(response);
  }
}
