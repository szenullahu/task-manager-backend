package ch.sze.Task_Manager.repository;

import ch.sze.Task_Manager.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, UUID> {
    // Finds all tasks belonging to a user by their user ID
    List<TaskEntity> findByUserEntityId(UUID userId);

}
