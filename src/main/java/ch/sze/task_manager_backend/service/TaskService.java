package ch.sze.task_manager_backend.service;

import ch.sze.task_manager_backend.entity.TaskEntity;
import ch.sze.task_manager_backend.entity.UserEntity;
import ch.sze.task_manager_backend.entity.dto.TaskDTO;
import ch.sze.task_manager_backend.entity.dto.TaskUpdateDTO;
import ch.sze.task_manager_backend.repository.TaskRepo;
import ch.sze.task_manager_backend.repository.UserEntityRepo;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    private final UserEntityRepo userEntityRepo;

    public TaskService(TaskRepo taskRepo, UserEntityRepo userEntityRepo) {
        this.taskRepo = taskRepo;
        this.userEntityRepo = userEntityRepo;
    }

    public TaskEntity getTaskById(UUID id) {
        return taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepo.findAll();
    }

    public TaskEntity createTask(TaskEntity taskEntity, UUID userId) {
        UserEntity userEntity = userEntityRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        taskEntity.setUserEntity(userEntity);
        return taskRepo.save(taskEntity);
    }


    public void deleteTask(UUID id, UUID userId) throws AccessDeniedException {

        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!Objects.equals(task.getUserEntity().getId(), userId)) {
            throw new AccessDeniedException("This is not your task.");
        }

        taskRepo.delete(task);
    }


    public TaskDTO mapToTaskDTO(TaskEntity task) {

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreated(),
                task.getUpdated()
        );
    }

    public List<TaskDTO> getMyTasks(UUID userId) {
        List<TaskEntity> tasks = taskRepo.findByUserEntityId(userId);
        return tasks.stream().map(this::mapToTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO updateTask(UUID id, TaskUpdateDTO dto, UUID userId) throws AccessDeniedException {
        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!Objects.equals(task.getUserEntity().getId(), userId)) {
            throw new AccessDeniedException("This is not your task.");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        TaskEntity updated = taskRepo.save(task);
        return mapToTaskDTO(updated);
    }
}
