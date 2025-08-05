package ch.sze.task_manager_backend.repository;

import ch.sze.task_manager_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, UUID> {

    UserEntity findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
