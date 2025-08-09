package ch.sze.task_manager_backend.entity.dto.task;

import ch.sze.task_manager_backend.entity.TaskPriority;
import ch.sze.task_manager_backend.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TaskUpdateDTO {

    @Size(min = 1, message = "Title cannot be empty")
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    @FutureOrPresent(message = "Due date must be today or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy")
    private Date dueDate;
}