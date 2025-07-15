package ch.sze.Task_Manager.controller;

import ch.sze.Task_Manager.entity.UserEntity;
import ch.sze.Task_Manager.service.UserEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserEntityController {

    private final UserEntityService service;

    public UserEntityController(UserEntityService service) {
        this.service = service;

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(service.createUser(userEntity), HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody UserEntity userEntity) {
        if (service.getUserId(id) != null) {
            service.updateUser(id, userEntity);
            return new ResponseEntity<>("User Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        if (service.getUserId(id) != null) {
            service.deleteUser(id);
            return new ResponseEntity<>("User Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
