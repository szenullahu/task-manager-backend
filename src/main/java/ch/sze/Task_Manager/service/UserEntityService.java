package ch.sze.Task_Manager.service;

import ch.sze.Task_Manager.entity.UserEntity;
import ch.sze.Task_Manager.repository.UserEntityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityService {

    private final UserEntityRepo repo;

    public UserEntityService(UserEntityRepo repo) {
        this.repo = repo;
    }

    public List<UserEntity> getAllUsers() {
        return repo.findAll();
    }

    public UserEntity createUser(UserEntity userEntity) {
        return repo.save(userEntity);
    }

    public UserEntity updateUser(long id, UserEntity userEntity) {
        UserEntity existingUserEntity = repo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        existingUserEntity.setUsername(userEntity.getUsername());
        existingUserEntity.setEmail(userEntity.getEmail());
        existingUserEntity.setPassword(userEntity.getPassword());
        existingUserEntity.setFirstname(userEntity.getFirstname());
        existingUserEntity.setSurname(userEntity.getSurname());
        return repo.save(existingUserEntity);
    }

    public UserEntity getUserId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteUser(long id) {
        repo.deleteById(id);
    }
}
