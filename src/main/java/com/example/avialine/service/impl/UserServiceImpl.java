package com.example.avialine.service.impl;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.exception.RoleNotFoundException;
import com.example.avialine.exception.UserAlreadyExistsException;
import com.example.avialine.exception.UserNotFoundException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.mapper.EntityMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.service.UserService;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
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
    public IamResponse<UserDTO> getUserById(@NotNull Integer id) throws UserNotFoundException {

        User user = userRepo.findById(id)
                .orElseThrow(() ->  new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_MESSAGE.getMessage(id)));

        UserDTO response = dtoMapper.toUserDTO(user);

        return IamResponse.createdSuccessfully(response);
    }

    @Override
    public IamResponse<UserDTO> createUser(@NotNull UserDTO userDTO) {
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

        UserDTO response = dtoMapper.toUserDTO(saved);

        return IamResponse.createdSuccessfully(response);
    }

    @Override
    public IamResponse<UserDTO> updateUserByEmail(@NotNull String email,@NotNull UserDTO dto) {
        User findUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email)));

        findUser.setName(dto.getUsername());
        findUser.setEmail(dto.getEmail());
        findUser.setPassword(dto.getPassword());
        findUser.setPhone(dto.getPhone());

        User updated = userRepo.save(findUser);

        UserDTO response = dtoMapper.toUserDTO(updated);

        return IamResponse.createdSuccessfully(response);
    }

    @Override
    public void deleteUserById(@NotNull Integer id) {
        User findUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_MESSAGE.getMessage(id)));

        findUser.setDeleted(true);

        userRepo.save(findUser);
    }
}
