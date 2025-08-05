package ch.sze.task_manager_backend.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank(message = "Surname cannot be blank")
    @Size(min = 2, max = 50)
    private String surname;
}
