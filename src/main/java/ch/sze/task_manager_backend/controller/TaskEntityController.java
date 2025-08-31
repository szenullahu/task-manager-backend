package ch.sze.task_manager_backend.controller;

import ch.sze.task_manager_backend.entity.TaskEntity;
import ch.sze.task_manager_backend.entity.dto.task.TaskCreateDTO;
import ch.sze.task_manager_backend.entity.dto.task.TaskDTO;
import ch.sze.task_manager_backend.entity.dto.task.TaskUpdateDTO;
import ch.sze.task_manager_backend.service.JWTService;
import ch.sze.task_manager_backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskEntityController {

    private final TaskService taskService;

    private final JWTService jwtService;

    public TaskEntityController(TaskService taskService, JWTService jwtService) {
        this.taskService = taskService;
        this.jwtService = jwtService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@RequestHeader("Authorization") String authHeader, @PathVariable UUID id) throws AccessDeniedException {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        return new ResponseEntity<>(taskService.getTaskById(userId, id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getMyTasks(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        return new ResponseEntity<>(taskService.getMyTasks(userId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        TaskDTO savedTask = taskService.createTask(taskCreateDTO, userId);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskUpdateDTO task, @RequestHeader("Authorization") String authHeader) throws AccessDeniedException {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        TaskDTO updatedTask = taskService.updateTask(id, task, userId);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) throws AccessDeniedException {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
