package com.example.tasktracker.controller;
import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.model.entity.Project;
import com.example.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //Post (Create new Project)
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project,
                                                 @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Project createdProject = projectService.createProject(project, currentUserId);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    //Get (Retrieve all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(@RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(projectService.searchProjectsByName(name));
        }
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    //Get(Retrieve a single project by its ID)
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> replaceProject(@PathVariable UUID id,
                                                  @Valid @RequestBody Project projectDetails,
                                                  @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Project replacedProject = projectService.replaceProject(id, projectDetails, currentUserId);
        return ResponseEntity.ok(replacedProject);
    }

    //Patch(Partial update of an existing project)
    @PatchMapping("/{id}")
    public ResponseEntity<Project> patchProject(@PathVariable UUID id,
                                                @RequestBody Project projectDetails,
                                                @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Project patchedProject = projectService.patchProject(id, projectDetails, currentUserId);
        return ResponseEntity.ok(patchedProject);
    }
    //Delete Project by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
