package dev.moon.programming.service;

import dev.moon.programming.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
  private final UserRoleRepository roleRepository;

  public UserRoleService(UserRoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }
}
