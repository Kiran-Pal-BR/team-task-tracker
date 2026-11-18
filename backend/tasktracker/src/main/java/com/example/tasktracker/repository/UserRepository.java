package com.example.tasktracker.repository;
import com.example.tasktracker.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    //To fetch user from the keyID
    Optional<User> findByKeycloakId(String keycloakId);

    // To check user from email id.
    Optional<User> findByEmailIgnoreCase(String email);

    //To check the username
    Optional<User> findByUsernameIgnoreCase(String username);
}
