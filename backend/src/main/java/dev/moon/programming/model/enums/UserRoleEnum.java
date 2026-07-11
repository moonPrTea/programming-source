package dev.moon.programming.model.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum UserRoleEnum {
  ADMIN,
  PARTICIPANT,
  ANALYTICS;

  public static Optional<UserRoleEnum> getRole(String userRole) {
    return Arrays.stream(values())
            .filter(role -> role.name().equals(userRole))
            .findFirst();
  }

  public List<GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + name()));
  }
}
