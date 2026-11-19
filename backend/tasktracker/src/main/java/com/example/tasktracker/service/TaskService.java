package com.example.tasktracker.service;
import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.enums.Priority;
import com.example.tasktracker.model.enums.Status;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //Create new task
    @Transactional
    public Task createTask(Task task, UUID currentUserId) {
        if (task == null) {
            throw new BadRequestException("Task payload cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new BadRequestException("Task title cannot be null or blank");
        }

        validateReferences(task.getProjectId(), task.getAssignedTo());

        if (task.getId() == null) {
            task.setId(UUID.randomUUID());
        }
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }

        task.setCreatedBy(currentUserId);
        task.setUpdatedBy(currentUserId);

        return taskRepository.save(task);
    }

    //Retrieve all the task
    public List<Task> getAllTasks(UUID projectId, UUID assignedTo, Status status) {
        if (projectId != null && status != null) {
            return taskRepository.findByProjectIdAndStatus(projectId, status);
        }
        if (projectId != null) {
            return taskRepository.findByProjectId(projectId);
        }
        if (assignedTo != null) {
            return taskRepository.findByAssignedTo(assignedTo);
        }
        return taskRepository.findAll();
    }
    //Retrieve a task by Id
    public Task getTaskById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Task ID cannot be null");
        }
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
    //PUT Full replacement of an existing task.
    @Transactional
    public Task replaceTask(UUID id, Task updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("Task ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated task details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }
        if (updatedDetails.getTitle() == null || updatedDetails.getTitle().isBlank()) {
            throw new BadRequestException("Task title cannot be null or blank");
        }

        Task existingTask = getTaskById(id);
        validateReferences(updatedDetails.getProjectId(), updatedDetails.getAssignedTo());

        existingTask.setProjectId(updatedDetails.getProjectId());
        existingTask.setAssignedTo(updatedDetails.getAssignedTo());
        existingTask.setTitle(updatedDetails.getTitle());
        existingTask.setDescription(updatedDetails.getDescription());
        existingTask.setStatus(updatedDetails.getStatus() != null ? updatedDetails.getStatus() : Status.TODO);
        existingTask.setPriority(updatedDetails.getPriority() != null ? updatedDetails.getPriority() : Priority.MEDIUM);
        existingTask.setDueDate(updatedDetails.getDueDate());
        existingTask.setUpdatedBy(currentUserId);

        return taskRepository.save(existingTask);
    }

    //Patch Partial update of an existing task.
    @Transactional
    public Task patchTask(UUID id, Task updatedDetails, UUID currentUserId) {
        if (id == null) {
            throw new BadRequestException("Task ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new BadRequestException("Updated task details cannot be null");
        }
        if (currentUserId == null) {
            throw new BadRequestException("Current user ID cannot be null");
        }

        Task existingTask = getTaskById(id);

        UUID targetProjectId = updatedDetails.getProjectId() != null
                ? updatedDetails.getProjectId()
                : existingTask.getProjectId();
        UUID targetAssignedTo = updatedDetails.getAssignedTo() != null
                ? updatedDetails.getAssignedTo()
                : existingTask.getAssignedTo();

        validateReferences(targetProjectId, targetAssignedTo);

        if (updatedDetails.getProjectId() != null) {
            existingTask.setProjectId(updatedDetails.getProjectId());
        }
        if (updatedDetails.getAssignedTo() != null) {
            existingTask.setAssignedTo(updatedDetails.getAssignedTo());
        }
        if (updatedDetails.getTitle() != null) {
            if (updatedDetails.getTitle().isBlank()) {
                throw new BadRequestException("Task title cannot be blank");
            }
            existingTask.setTitle(updatedDetails.getTitle());
        }
        if (updatedDetails.getDescription() != null) {
            existingTask.setDescription(updatedDetails.getDescription());
        }
        if (updatedDetails.getStatus() != null) {
            existingTask.setStatus(updatedDetails.getStatus());
        }
        if (updatedDetails.getPriority() != null) {
            existingTask.setPriority(updatedDetails.getPriority());
        }
        if (updatedDetails.getDueDate() != null) {
            existingTask.setDueDate(updatedDetails.getDueDate());
        }

        existingTask.setUpdatedBy(currentUserId);

        return taskRepository.save(existingTask);
    }

    //Delete by ID
    @Transactional
    public void deleteTask(UUID id) {
        if (id == null) {
            throw new BadRequestException("Task ID cannot be null for deletion");
        }
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    private void validateReferences(UUID projectId, UUID assignedTo) {
        if (projectId == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        if (assignedTo != null && !userRepository.existsById(assignedTo)) {
            throw new ResourceNotFoundException("Assigned user not found with id: " + assignedTo);
        }
    }


}
