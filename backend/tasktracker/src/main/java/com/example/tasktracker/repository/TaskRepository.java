package com.example.tasktracker.repository;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.enums.Priority;
import com.example.tasktracker.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectId(UUID projectId);
    List<Task> findByAssignedTo(UUID assignedTo);
    List<Task> findByProjectIdAndStatus(UUID projectId, Status status);


}
