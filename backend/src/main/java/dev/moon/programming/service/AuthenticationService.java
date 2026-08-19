package dev.moon.programming.service;

import dev.moon.programming.dto.auth.request.AuthenticationRequest;
import dev.moon.programming.dto.auth.request.RegistrationRequest;
import dev.moon.programming.dto.auth.response.AuthenticationResponse;
import dev.moon.programming.model.entity.UserEntity;
import dev.moon.programming.model.entity.UserRoleEntity;
import dev.moon.programming.model.enums.TokenType;
import dev.moon.programming.model.enums.UserRoleEnum;
import dev.moon.programming.repository.UserRepository;
import dev.moon.programming.repository.UserRoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthenticationService {

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserRoleRepository roleRepository;
  private final AuthenticationManager authManager;
  private final RefreshTokenService refreshTokenService;

  public AuthenticationService(PasswordEncoder passwordEncoder,
          JwtService jwtService, UserRepository userRepository,
          UserRoleRepository roleRepository, AuthenticationManager authManager,
          RefreshTokenService refreshTokenService) {
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.authManager = authManager;
    this.refreshTokenService = refreshTokenService;
  }

  @Transactional
  public AuthenticationResponse register(RegistrationRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("User with current username already exists");
    }
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("User with current email already exists");
    }

    UserRoleEntity role = roleRepository.getByRole(UserRoleEnum.PARTICIPANT);

    UserEntity user = new UserEntity(request.firstName(), request.lastName(), request.middleName(), request.username(),
            request.email(), request.birthDate(), null);
    user.setPswHash(passwordEncoder.encode(request.password()));
    user.setRole(role);

    user = userRepository.save(user);

    return createAuthorizationResponse(user);
  }

  @Transactional
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
    );

    UserEntity user = userRepository.findByUsername(request.username())
            .or(() -> userRepository.findByEmail(request.username()))
            .orElseThrow(() -> new IllegalStateException("Not found user record"));

    return createAuthorizationResponse(user);
  }

  private AuthenticationResponse createAuthorizationResponse(UserEntity user) {
    String accessToken = jwtService.generateToken(user);
    String refreshToken = refreshTokenService.createRefreshToken(user.getId());

    List<String> roles = user.getAuthorities().stream()
            .map(SimpleGrantedAuthority.class::cast)
            .map(SimpleGrantedAuthority::getAuthority)
            .toList();

    return new AuthenticationResponse(user.getId(), user.getEmail(), roles, accessToken, refreshToken, TokenType.BEARER.name());
  }
}
