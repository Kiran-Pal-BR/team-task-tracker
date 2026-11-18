package com.example.tasktracker.repository;
import com.example.tasktracker.model.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>{
    // To search by Project Name(Case insensitive and partial search)
    List<Project> findByNameContainingIgnoreCase(String name);

    // To fetch projects created by a specific user.
    List<Project> findByCreatedBy(UUID userId);

    //To fetch last update in the project by a specific user
    List<Project> findByUpdatedBy(UUID userId);
}
