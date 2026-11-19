package com.example.tasktracker.controller;

import com.example.tasktracker.exception.BadRequestException;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.enums.Status;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


     //Post Create a new task

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task,
                                           @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Task createdTask = taskService.createTask(task, currentUserId);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }


     // Get Retrieve all tasks

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) UUID assignedTo,
            @RequestParam(required = false) Status status) {
        return ResponseEntity.ok(taskService.getAllTasks(projectId, assignedTo, status));
    }


     // Get: Retrieve a single task by ID

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

     // Put: Full replacement of an existing task

    @PutMapping("/{id}")
    public ResponseEntity<Task> replaceTask(@PathVariable UUID id,
                                            @Valid @RequestBody Task taskDetails,
                                            @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Task replacedTask = taskService.replaceTask(id, taskDetails, currentUserId);
        return ResponseEntity.ok(replacedTask);
    }


     // Patch: Partial update of an existing task

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable UUID id,
                                          @RequestBody Task taskDetails,
                                          @RequestHeader(value = "X-User-Id", required = false) UUID currentUserId) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing mandatory 'X-User-Id' header");
        }
        Task patchedTask = taskService.patchTask(id, taskDetails, currentUserId);
        return ResponseEntity.ok(patchedTask);
    }


     // Delete Delete a task by ID

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}