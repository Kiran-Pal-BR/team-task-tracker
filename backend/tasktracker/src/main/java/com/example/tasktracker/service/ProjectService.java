package com.example.tasktracker.service;

import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.model.entity.Project;
import com.example.tasktracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //Create new project
    @Transactional
    public Project createProject(Project project, UUID currentUserId) {
        if (project == null) {
            throw new BadRequestException("Project payload cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }
        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }

        project.setCreatedBy(currentUserId);
        project.setUpdatedBy(currentUserId);

        return projectRepository.save(project);
    }


    // PUT: Full replacement of an existing project.

    @Transactional
    public Project replaceProject(UUID id, Project updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated project details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }
        if (updatedDetails.getName() == null || updatedDetails.getName().isBlank()) {
            throw new BadRequestException("Project name cannot be null or blank");
        }

        Project existingProject = getProjectById(id);

        existingProject.setName(updatedDetails.getName());
        existingProject.setDescription(updatedDetails.getDescription());
        existingProject.setUpdatedBy(currentUserId);

        return projectRepository.save(existingProject);
    }


     //PATCH: Partial update of an existing project.

    @Transactional
    public Project patchProject(UUID id, Project updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated project details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }

        Project existingProject = getProjectById(id);

        if (updatedDetails.getName() != null) {
            if (updatedDetails.getName().isBlank()) {
                throw new BadRequestException("Project name cannot be blank");
            }
            existingProject.setName(updatedDetails.getName());
        }
        if (updatedDetails.getDescription() != null) {
            existingProject.setDescription(updatedDetails.getDescription());
        }
        existingProject.setUpdatedBy(currentUserId);

        return projectRepository.save(existingProject);
    }


     //Retrieve all projects.

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


     // Retrieve a project by ID.

    public Project getProjectById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }


     //Search projects by name (case-insensitive).

    public List<Project> searchProjectsByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return projectRepository.findByNameContainingIgnoreCase(name);
    }


     //Delete a project by ID.

    @Transactional
    public void deleteProject(UUID id) {
        if (id == null) {
            throw new BadRequestException("Project ID cannot be null for deletion");
        }
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}