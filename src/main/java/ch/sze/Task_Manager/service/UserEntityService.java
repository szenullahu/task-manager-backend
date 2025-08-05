package ch.sze.Task_Manager.service;

import ch.sze.Task_Manager.entity.UserEntity;
import ch.sze.Task_Manager.entity.UserPrincipal;
import ch.sze.Task_Manager.entity.dto.PasswordUpdateDTO;
import ch.sze.Task_Manager.entity.dto.UserDTO;
import ch.sze.Task_Manager.entity.dto.UserUpdateDTO;
import ch.sze.Task_Manager.repository.UserEntityRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserEntity> getAllUsers() {
        return userEntityRepo.findAll();
    }

    public UserEntity register(UserEntity userEntity) {
        if (userEntityRepo.existsByUsername(userEntity.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userEntityRepo.existsByEmail(userEntity.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return userEntityRepo.save(userEntity);
    }

    public String login(UserEntity userEntity) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEntity.getUsername(),
                        userEntity.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(principal.getUserEntity()); // or principal.getUserEntity().getId()
    }


    public UserEntity getUserId(UUID id) {
        return userEntityRepo.findById(id).orElse(null);
    }

    public void deleteUser(UUID userId) {
        UserEntity user = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userEntityRepo.delete(user);
    }

    public List<UserDTO> getAllUserDTOs() {
        return userEntityRepo.findAll().stream()
                .map(this::mapToUserDTO)
                .toList();
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


    public UserDTO getUser(UUID userId) {
        UserEntity user = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("Could Not Find User"));
        return mapToUserDTO(user);
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
        UserEntity userEntity = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("Could Not Find User"));

        if(!encoder.matches(passwordUpdateDTO.getOldPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        userEntity.setPassword(encoder.encode(passwordUpdateDTO.getNewPassword()));
        userEntityRepo.save(userEntity);
    }
}
