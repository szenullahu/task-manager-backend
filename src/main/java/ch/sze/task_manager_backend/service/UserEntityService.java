package ch.sze.task_manager_backend.service;

import ch.sze.task_manager_backend.entity.UserEntity;
import ch.sze.task_manager_backend.entity.UserPrincipal;
import ch.sze.task_manager_backend.entity.dto.user.*;
import ch.sze.task_manager_backend.repository.UserEntityRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserEntityService {

    private final UserEntityRepo userEntityRepo;

    private final PasswordEncoder encoder;

    private final AuthenticationManager manager;

    private final JWTService jwtService;

    public UserEntityService(UserEntityRepo userEntityRepo, PasswordEncoder encoder, AuthenticationManager manager, JWTService jwtService) {
        this.userEntityRepo = userEntityRepo;
        this.encoder = encoder;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    public UserDTO getUser(UUID userId) {
        UserEntity user = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("Could Not Find User"));
        return mapToUserDTO(user);
    }

    public UserDTO register(UserRegisterDTO dto) {
        if (userEntityRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userEntityRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }
        UserEntity userEntity = mapToEntity(dto);
        userEntity.setPassword(encoder.encode(dto.getPassword()));
        userEntityRepo.save(userEntity);
        return mapToUserDTO(userEntity);
    }

    public String login(UserLoginDTO dto) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(principal.getUserEntity()); // or principal.getUserEntity().getId()
    }


    public UserDTO updateUserEntity(UUID userId, UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("Could Not Find User"));
        String newEmail = userUpdateDTO.getEmail();
        if (!userEntity.getEmail().equals(newEmail) && userEntityRepo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        userEntity.setEmail(userUpdateDTO.getEmail());
        userEntity.setFirstname(userUpdateDTO.getFirstname());
        userEntity.setSurname(userUpdateDTO.getSurname());
        userEntityRepo.save(userEntity);
        return mapToUserDTO(userEntity);

    }

    public void updateUserPassword(UUID userId, PasswordUpdateDTO passwordUpdateDTO) {
        UserEntity userEntity = userEntityRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!encoder.matches(passwordUpdateDTO.getOldPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        userEntity.setPassword(encoder.encode(passwordUpdateDTO.getNewPassword()));
        userEntityRepo.save(userEntity);
    }

    public void deleteUser(UUID userId) {
        UserEntity user = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userEntityRepo.delete(user);
    }

    public UserDTO mapToUserDTO(UserEntity user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getSurname()
        );
    }

    private UserEntity mapToEntity(UserRegisterDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.getUsername());
        userEntity.setEmail(dto.getEmail());
        userEntity.setFirstname(dto.getFirstname());
        userEntity.setSurname(dto.getSurname());
        // Password is set separately after encoding
        return userEntity;
    }
}