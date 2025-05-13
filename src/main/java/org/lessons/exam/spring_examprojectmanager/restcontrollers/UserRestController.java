package org.lessons.exam.spring_examprojectmanager.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.lessons.exam.spring_examprojectmanager.models.User;
import org.lessons.exam.spring_examprojectmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    //READ
    @GetMapping
    public ResponseEntity<List<User>> usersRestIndex() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> usersRestShow(@PathVariable Integer id) {
        Optional<User> userOptional = userService.optionalFindById(id);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //CREATE
    //optionally send all roles in a list to the frontend  
    // @PostMapping("/store")
    // public ResponseEntity<User> usersRestStore(@Valid @RequestBody User userToStore) {
    //     User userStored = userService.create(userToStore);
    //     return new ResponseEntity<>(userStored, HttpStatus.CREATED);
    // }


    // //UPDATE
    // @PutMapping("/{id}")
    // public ResponseEntity<User> usersRestUpdate(@Valid @RequestBody User userToUpdate, @PathVariable Integer id) {
    //     if (!userService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     userToUpdate.setId(id);
    //     User userUpdated = userService.edit(userToUpdate);
    //     return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    // }


    // //DELETE
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> usersRestDelete(@PathVariable Integer id) {
    //     if (!userService.boolExistsById(id)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     userService.deleteById(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }


}
