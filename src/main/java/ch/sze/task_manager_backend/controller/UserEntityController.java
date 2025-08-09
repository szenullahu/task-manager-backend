package ch.sze.task_manager_backend.controller;

import ch.sze.task_manager_backend.entity.UserEntity;
import ch.sze.task_manager_backend.entity.dto.user.*;
import ch.sze.task_manager_backend.service.JWTService;
import ch.sze.task_manager_backend.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserEntityController {

    private final UserEntityService userService;
    private final JWTService jwtService;

    public UserEntityController(UserEntityService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUserDTOs(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        UserDTO user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        return new ResponseEntity<>(userService.register(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UserUpdateDTO user) throws AccessDeniedException {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        UserDTO updatedUser = userService.updateUserEntity(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        userService.updateUserPassword(userId, passwordUpdateDTO);
        return ResponseEntity.ok("Password updated successfully");
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
