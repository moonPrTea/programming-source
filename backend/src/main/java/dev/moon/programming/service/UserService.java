package dev.moon.programming.service;

import dev.moon.programming.dto.user.request.CreateUserRequest;
import dev.moon.programming.dto.user.response.UserRoleResponse;
import dev.moon.programming.mapper.UserMapper;
import dev.moon.programming.model.entity.UserEntity;
import dev.moon.programming.model.entity.UserRoleEntity;
import dev.moon.programming.model.enums.UserRoleEnum;
import dev.moon.programming.repository.UserRepository;
import dev.moon.programming.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserRoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                     UserMapper userMapper, UserRoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.roleRepository = roleRepository;
  }

  @Transactional(readOnly = true)
  public UserRoleResponse getUser(UUID id) {
    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Not found user by id"));

    return userMapper.toUserRoleResponse(user);
  }

  @Transactional
  public UserRoleResponse createUser(CreateUserRequest userRequest) {
    UserRoleEnum roleEnum = UserRoleEnum.getRole(userRequest.role())
            .orElseThrow(() -> new EntityNotFoundException("Not found user role"));
    UserRoleEntity userRole = roleRepository.getByRole(roleEnum);

    UserEntity user = userMapper.toEntity(userRequest);

    userRepository.save(user);

    return userMapper.toUserRoleResponse(user);
  }

  @Transactional
  public void deleteUser(UUID id) {
    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Not found user by id"));

    userRepository.delete(user);
  }
}
