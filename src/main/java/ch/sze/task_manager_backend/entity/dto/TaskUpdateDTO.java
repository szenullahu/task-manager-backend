package ch.sze.task_manager_backend.entity.dto;

import ch.sze.task_manager_backend.entity.TaskPriority;
import ch.sze.task_manager_backend.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskUpdateDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy")
    private Date dueDate;
}