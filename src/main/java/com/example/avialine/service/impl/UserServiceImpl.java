package com.example.avialine.service.impl;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.exception.*;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.mapper.EntityMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final DTOMapper dtoMapper;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(@NotNull Integer id) throws UserNotFoundException {

        User user = userRepo.findById(id)
                .orElseThrow(() ->  new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_MESSAGE.getMessage(id)));



        return dtoMapper.toUserDTO(user);
    }

    @Override
    public UserDTO createUser(@NotNull UserDTO userDTO) {
        if (userRepo.existsByName(userDTO.getUsername()) || userRepo.existsByEmail(userDTO.getEmail())){
            throw new UserAlreadyExistsException(ApiErrorMessage.USER_ALREADY_EXISTS_MESSAGE.getMessage(userDTO.getEmail()));
        }

        User user = entityMapper.toUser(userDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCreatedAt(Instant.now());

        Role roles = roleRepo.findByName("ROLE_USER")
                        .orElseThrow(() -> new RoleNotFoundException(ApiErrorMessage.ROLE_NOT_FOUND_BY_ID_MESSAGE.getMessage("ROLE_USER")));
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roles);

        user.setRoles(roleSet);

        User saved = userRepo.save(user);

        return dtoMapper.toUserDTO(saved);
    }

    @Override
    public UserDTO updateUserByEmail(@NotNull String email,@NotNull UserDTO dto) {
        User findUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email)));

        findUser.setName(dto.getUsername());
        findUser.setEmail(dto.getEmail());
        findUser.setPassword(dto.getPassword());
        findUser.setPhone(dto.getPhone());

        User updated = userRepo.save(findUser);

        return dtoMapper.toUserDTO(updated);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(@NotNull String email) {
        User findUser = userRepo.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                ApiErrorMessage
                                        .USER_NOT_FOUND_BY_EMAIL_MESSAGE
                                        .getMessage(email)
                        )

                );

        findUser.setDeleted(true);

        userRepo.save(findUser);
    }

    @Transactional
    @Override
    public User getActiveUserByEmail(String email) {
        return userRepo.findActiveUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email))
                );

    }

    @Transactional(readOnly = true)
    @Override
    public User getActiveUserByPhone(String phone) {
        return userRepo.findActiveUserByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException(
                        ApiErrorMessage.USER_NOT_FOUND_BY_PHONE_MESSAGE.getMessage(phone)
                )
                );
    }
}
