package ch.sze.Task_Manager.repository;

import ch.sze.Task_Manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {

}
