package dev.moon.programming.mapper;

import dev.moon.programming.dto.user.request.CreateUserRequest;
import dev.moon.programming.dto.user.response.UserRoleResponse;
import dev.moon.programming.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserEntity toEntity(CreateUserRequest request) {
    return new UserEntity(request.username(), request.email(), request.birthDate(), request.description());
  }

  public UserRoleResponse toUserRoleResponse(UserEntity user) {
    return new UserRoleResponse(user.getUsername(), user.getEmail(), user.getRole().getRole().name(),
            user.getDescription(), user.getBirthDate());
  }
}
