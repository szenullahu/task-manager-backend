package ch.sze.task_manager_backend.controller;

import ch.sze.task_manager_backend.entity.TaskEntity;
import ch.sze.task_manager_backend.entity.dto.TaskDTO;
import ch.sze.task_manager_backend.entity.dto.TaskUpdateDTO;
import ch.sze.task_manager_backend.service.JWTService;
import ch.sze.task_manager_backend.service.TaskService;
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

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getMyTasks(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        return new ResponseEntity<>(taskService.getMyTasks(userId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskEntity taskEntity, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = jwtService.extractId(token);
        TaskEntity savedTask = taskService.createTask(taskEntity, userId);
        TaskDTO dto = taskService.mapToTaskDTO(savedTask);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID id, @RequestBody TaskUpdateDTO task, @RequestHeader("Authorization") String authHeader) throws AccessDeniedException {
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
        return ResponseEntity.ok("Task deleted successfully");
    }
}
