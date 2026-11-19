package com.example.tasktracker.controller;
import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.model.entity.ProjectMember;
import com.example.tasktracker.service.ProjectMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    //Add a user to a project
    @PostMapping
    public ResponseEntity<ProjectMember> addMember(@RequestParam UUID projectId,
                                                   @RequestParam UUID userId) {
        ProjectMember created = projectMemberService.addMember(projectId, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //Get List members by projectId or projects by userId
    @GetMapping
    public ResponseEntity<List<ProjectMember>> getMemberships(
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) UUID userId) {
        if (projectId != null) {
            return ResponseEntity.ok(projectMemberService.getMembersByProjectId(projectId));
        }
        if (userId != null) {
            return ResponseEntity.ok(projectMemberService.getProjectsByUserId(userId));
        }
        throw new BadRequestException("Either 'projectId' or 'userId' query parameter must be provided");
    }

    //Delete to remove user from a project
    @DeleteMapping
    public ResponseEntity<Void> removeMember(@RequestParam UUID projectId,
                                             @RequestParam UUID userId) {
        projectMemberService.removeMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }

}
