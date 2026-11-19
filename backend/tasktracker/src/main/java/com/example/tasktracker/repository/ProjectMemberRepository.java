package com.example.tasktracker.repository;
import com.example.tasktracker.model.entity.ProjectMember;
import com.example.tasktracker.model.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId>{
    //To look all the members of the project
    List<ProjectMember> findById_projectId(UUID projectId);

    //TO get the list of a specific user
    List<ProjectMember> findById_userId(UUID userId);
}
