package com.example.tasktracker.controller;

import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


 // Post Create a new user

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user,
                                           @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        User createdUser = userService.createUser(user, currentUserId);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

      //Get Retrieve all users or filter by email / keycloakId

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String keycloakId) {
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(List.of(userService.getUserByEmailIgnoreCase(email)));
        }
        if (keycloakId != null && !keycloakId.isBlank()) {
            return ResponseEntity.ok(List.of(userService.getUserByKeycloakId(keycloakId)));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }


     //Get Retrieve a single user by UUID

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    //Put Full replacement of an existing user

    @PutMapping("/{id}")
    public ResponseEntity<User> replaceUser(@PathVariable UUID id,
                                            @Valid @RequestBody User userDetails,
                                            @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        User replacedUser = userService.replaceUser(id, userDetails, currentUserId);
        return ResponseEntity.ok(replacedUser);
    }


    //Patch Partial update of an existing user

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable UUID id,
                                          @RequestBody User userDetails,
                                          @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        User patchedUser = userService.patchUser(id, userDetails, currentUserId);
        return ResponseEntity.ok(patchedUser);
    }


     // Delete a user by UUID

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}