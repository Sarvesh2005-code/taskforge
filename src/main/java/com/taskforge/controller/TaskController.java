package com.taskforge.controller;

import com.taskforge.dto.request.TaskRequest;
import com.taskforge.dto.response.TaskResponse;
import com.taskforge.service.TaskService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REFACTORED TASK CONTROLLER — Now thin and clean!
 * =================================================
 *
 * BEFORE (Module 2):
 *   Controller stored data in ArrayList + contained all logic.
 *   (~120 lines of code)
 *
 * AFTER (Module 3):
 *   Controller only handles HTTP concerns and delegates to TaskService.
 *   (~50 lines of code)
 *
 * This is the PROPER way. Each method:
 *   1. Reads the HTTP request (path variables, request body)
 *   2. Calls the service method
 *   3. Returns the response with the right status code
 *
 * No business logic. No database access. Just HTTP glue.
 *
 * DEPENDENCY INJECTION:
 * The constructor takes TaskService as a parameter.
 * Spring automatically injects the TaskService bean.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Spring injects the TaskService bean here automatically.
    // No @Autowired needed when there's only one constructor.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks → List all tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * GET /api/tasks/{id} → Get one task
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * POST /api/tasks → Create a new task
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        // @Valid tells Spring: "Validate this object using the annotations on TaskRequest"
        // If validation fails, Spring throws MethodArgumentNotValidException
        // BEFORE this method even runs. Our GlobalExceptionHandler catches it.
        TaskResponse created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tasks/{id} → Update an existing task
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    /**
     * DELETE /api/tasks/{id} → Delete a task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
