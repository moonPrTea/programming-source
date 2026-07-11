package dev.moon.programming.model.entity;

import dev.moon.programming.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Entity(name = "user_roles")
public class UserRoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRoleEnum role;

  public UserRoleEntity() {
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  public UserRoleEntity(UserRoleEnum role) {
    this.role = role;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserRoleEnum getRole() {
    return role;
  }

  public void setRole(UserRoleEnum role) {
    this.role = role;
  }
}
