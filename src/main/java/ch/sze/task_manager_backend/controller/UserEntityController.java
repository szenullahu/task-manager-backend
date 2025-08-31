package ch.sze.task_manager_backend.controller;

import ch.sze.task_manager_backend.entity.dto.user.*;
import ch.sze.task_manager_backend.service.JWTService;
import ch.sze.task_manager_backend.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authHeader) {
        UUID userId = jwtService.extractId(authHeader.substring(7));
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
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
        return ResponseEntity.ok(userService.updateUserEntity(userId, user));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        userService.updateUserPassword(userId, passwordUpdateDTO);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
