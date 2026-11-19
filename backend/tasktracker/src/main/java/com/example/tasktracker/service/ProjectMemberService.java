package com.example.tasktracker.service;
import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.model.entity.ProjectMember;
import com.example.tasktracker.model.entity.ProjectMemberId;
import com.example.tasktracker.repository.ProjectMemberRepository;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository,
                                ProjectRepository projectRepository,
                                UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //Add a user as a member to a project
    @Transactional
    public ProjectMember addMember(UUID projectId, UUID userId){
        if (projectId == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (userId == null) {
            throw new BadRequestException("User ID cannot be null");
        }
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        ProjectMemberId memberId = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();

        if (projectMemberRepository.existsById(memberId)) {
            throw new BadRequestException("User " + userId + " is already a member of project " + projectId);
        }

        ProjectMember member = ProjectMember.builder()
                .id(memberId)
                .build();

        return projectMemberRepository.save(member);
    }
    //Get all Members of a specific project
    public List<ProjectMember> getMembersByProjectId(UUID projectId) {
        if (projectId == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return projectMemberRepository.findById_projectId(projectId);
    }
    //Get all project memberships for a specific user
    public List<ProjectMember> getProjectsByUserId(UUID userId) {
        if (userId == null) {
            throw new BadRequestException("User ID cannot be null");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return projectMemberRepository.findById_userId(userId);
    }
    //Remove a member from a project
    @Transactional
    public void removeMember(UUID projectId, UUID userId) {
        if (projectId == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (userId == null) {
            throw new BadRequestException("User ID cannot be null");
        }

        ProjectMemberId memberId = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();

        if (!projectMemberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException(
                    "Project membership not found for projectId: " + projectId + " and userId: " + userId);
        }

        projectMemberRepository.deleteById(memberId);
    }
}
