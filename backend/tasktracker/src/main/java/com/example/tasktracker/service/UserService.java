package com.example.tasktracker.service;

import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Create a new User
    @Transactional
    public User createUser(User user, UUID currentUserId) {
        if (user == null) {
            throw new BadRequestException("User payload cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        validateUniqueFields(user, null);

        user.setCreatedBy(currentUserId);
        user.setUpdatedBy(currentUserId);

        return userRepository.save(user);
    }
    //Retrieve all users
    public List<User>getAllUsers(){
        return userRepository.findAll();
    }

    //Retrieve a single user by UUID
    public User getUserById(UUID id){
        if (id == null){
            throw new BadRequestException("User id cannot be null");
        }
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));
    }

    //Retrieve a use by email
    public User getUserByEmailIgnoreCase(String email){
        if (email == null || email.isBlank()){
            throw new BadRequestException("Email cannot be blank");
        }
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new ResourceNotFoundException("USer not found with email: "+ email));
    }

    //Retrieve User by keycloak ID
    public User getUserByKeycloakId(String keycloakId){
        if (keycloakId == null || keycloakId.isBlank()){
            throw  new BadRequestException("Key cloak ID cannot be blank");
        }
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with keycloak ID: "+ keycloakId));
    }


    //PUT: Full replacement of user details
    @Transactional
    public User replaceUser(UUID id, User updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("User ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated user details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }

        User existingUser = getUserById(id);
        validateUniqueFields(updatedDetails, id);

        existingUser.setUsername(updatedDetails.getUsername());
        existingUser.setEmail(updatedDetails.getEmail());
        existingUser.setKeycloakId(updatedDetails.getKeycloakId());
        existingUser.setUpdatedBy(currentUserId);

        return userRepository.save(existingUser);
    }

    //Patch: Partial Update user details.
    @Transactional
    public User patchUser(UUID id, User updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("User ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated user details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }

        User existingUser = getUserById(id);
        validateUniqueFields(updatedDetails, id);

        if (updatedDetails.getUsername() != null && !updatedDetails.getUsername().isBlank()) {
            existingUser.setUsername(updatedDetails.getUsername());
        }
        if (updatedDetails.getEmail() != null && !updatedDetails.getEmail().isBlank()) {
            existingUser.setEmail(updatedDetails.getEmail());
        }
        if (updatedDetails.getKeycloakId() != null && !updatedDetails.getKeycloakId().isBlank()) {
            existingUser.setKeycloakId(updatedDetails.getKeycloakId());
        }
        existingUser.setUpdatedBy(currentUserId);

        return userRepository.save(existingUser);
    }

    //Delete user by UUID
    public void deleteUser(UUID id){
        if (id == null){
            throw new BadRequestException("User Id cannot be null for deletion");
        }
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private void validateUniqueFields(User candidate, UUID currentUserId) {
        if (candidate.getUsername() != null && !candidate.getUsername().isBlank()) {
            userRepository.findByUsernameIgnoreCase(candidate.getUsername())
                    .filter(existing -> currentUserId == null || !existing.getId().equals(currentUserId))
                    .ifPresent(existing -> {
                        throw new BadRequestException("Username already exists: " + candidate.getUsername());
                    });
        }
        if (candidate.getEmail() != null && !candidate.getEmail().isBlank()) {
            userRepository.findByEmailIgnoreCase(candidate.getEmail())
                    .filter(existing -> currentUserId == null || !existing.getId().equals(currentUserId))
                    .ifPresent(existing -> {
                        throw new BadRequestException("Email already exists: " + candidate.getEmail());
                    });
        }
        if (candidate.getKeycloakId() != null && !candidate.getKeycloakId().isBlank()) {
            userRepository.findByKeycloakId(candidate.getKeycloakId())
                    .filter(existing -> currentUserId == null || !existing.getId().equals(currentUserId))
                    .ifPresent(existing -> {
                        throw new BadRequestException("Keycloak ID already exists: " + candidate.getKeycloakId());
                    });
        }
    }

}
