package dev.moon.programming.repository;

import dev.moon.programming.model.entity.UserRoleEntity;
import dev.moon.programming.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {
  UserRoleEntity getByRole(UserRoleEnum role);
}
