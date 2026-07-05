package com.taskforge.controller;

import com.taskforge.dto.request.TaskRequest;
import com.taskforge.dto.response.TaskResponse;
import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TASK CONTROLLER — The heart of our API!
 * ========================================
 * This controller handles ALL task-related HTTP requests.
 *
 * REST Convention:
 *   GET    /api/tasks       → List all tasks
 *   GET    /api/tasks/{id}  → Get one task by ID
 *   POST   /api/tasks       → Create a new task
 *   PUT    /api/tasks/{id}  → Update an existing task
 *   DELETE /api/tasks/{id}  → Delete a task
 *
 * @RequestMapping("/api/tasks")
 *   This is a CLASS-LEVEL mapping. It means ALL endpoints in this class
 *   start with "/api/tasks". So @GetMapping("/{id}") becomes GET /api/tasks/{id}.
 *   This avoids repeating "/api/tasks" on every method.
 *
 * NOTE: In this module, we're storing tasks in an ArrayList (in memory).
 *       When you restart the app, all data is lost!
 *       In Module 3, we'll replace this with a REAL database.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // ====================================================================
    // TEMPORARY IN-MEMORY STORAGE
    // This ArrayList acts as a fake database for now.
    // In Module 3, we'll replace this with JPA + H2 database.
    // ====================================================================
    private final List<TaskResponse> tasks = new ArrayList<>();
    private Long nextId = 1L;  // Simple ID counter (database does this automatically)

    // ====================================================================
    // CONSTRUCTOR — Pre-populate with sample data so we have something to see
    // ====================================================================
    public TaskController() {
        // Let's add 3 sample tasks so the API isn't empty when we start
        tasks.add(new TaskResponse(nextId++, "Set up Spring Boot project",
                "Initialize the project with Spring Initializr",
                TaskStatus.DONE, TaskPriority.HIGH,
                LocalDateTime.now().minusDays(1), LocalDateTime.now()));

        tasks.add(new TaskResponse(nextId++, "Build REST API endpoints",
                "Create CRUD operations for tasks",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                LocalDateTime.now(), LocalDateTime.now()));

        tasks.add(new TaskResponse(nextId++, "Connect to database",
                "Replace ArrayList with JPA + H2",
                TaskStatus.TODO, TaskPriority.MEDIUM,
                LocalDateTime.now(), LocalDateTime.now()));
    }

    // ====================================================================
    // GET /api/tasks — List ALL tasks
    // ====================================================================
    /**
     * @GetMapping (no path) → maps to the class-level path: GET /api/tasks
     *
     * We return the entire list of tasks.
     * Spring automatically converts the List<TaskResponse> to a JSON array:
     *
     *   [
     *     { "id": 1, "title": "...", "status": "TODO", ... },
     *     { "id": 2, "title": "...", "status": "DONE", ... }
     *   ]
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(tasks);
        // ResponseEntity.ok() = HTTP 200 OK + the data
        // It's the same as: return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // ====================================================================
    // GET /api/tasks/{id} — Get ONE task by its ID
    // ====================================================================
    /**
     * @PathVariable Long id — extracts the {id} from the URL.
     *
     * Example: GET /api/tasks/2  →  id = 2
     *
     * We search the list for a task with matching ID.
     * If found → return it with 200 OK
     * If not found → return 404 NOT FOUND
     *
     * What is Optional<T>?
     * --------------------
     * Optional is Java's way of saying "this might be null".
     * Instead of returning null (which causes NullPointerException),
     * we return Optional.empty() and handle it gracefully.
     *
     * .stream() → converts the list to a Stream (a sequence you can filter/map)
     * .filter() → keeps only elements that match the condition
     * .findFirst() → returns the first match (or empty if none found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Optional<TaskResponse> task = tasks.stream()
                .filter(t -> t.id().equals(id))  // Find where task ID matches
                .findFirst();

        // If task exists → return 200 with the task
        // If not → return 404 with no body
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ====================================================================
    // POST /api/tasks — Create a NEW task
    // ====================================================================
    /**
     * @PostMapping → handles HTTP POST requests (used for CREATING data)
     *
     * @RequestBody TaskRequest request
     *   This is the KEY annotation! It tells Spring:
     *   "Take the JSON from the request body and convert it into a TaskRequest object"
     *
     *   The client sends JSON like:
     *   {
     *     "title": "Learn Spring Boot",
     *     "description": "Build a task management API",
     *     "status": "TODO",
     *     "priority": "HIGH"
     *   }
     *
     *   Spring + Jackson automatically converts this JSON → TaskRequest object.
     *   This is called DESERIALIZATION (JSON → Java object).
     *
     * We return:
     *   - HTTP 201 CREATED (not 200 OK — 201 is the proper code for "resource created")
     *   - The created task (with the auto-generated ID and timestamps)
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        // Build a TaskResponse from the request data + auto-generated fields
        TaskResponse newTask = new TaskResponse(
                nextId++,                    // Auto-increment ID
                request.title(),             // From the request body
                request.description(),       // From the request body
                request.status() != null ? request.status() : TaskStatus.TODO,  // Default to TODO
                request.priority() != null ? request.priority() : TaskPriority.MEDIUM, // Default to MEDIUM
                LocalDateTime.now(),         // Auto-set creation time
                LocalDateTime.now()          // Auto-set update time
        );

        tasks.add(newTask);  // Add to our "database" (ArrayList)

        // Return 201 CREATED with the new task
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    // ====================================================================
    // PUT /api/tasks/{id} — Update an EXISTING task
    // ====================================================================
    /**
     * @PutMapping → handles HTTP PUT requests (used for UPDATING data)
     *
     * PUT vs PATCH:
     *   - PUT    = replace the ENTIRE resource (send ALL fields)
     *   - PATCH  = update only SOME fields (partial update)
     *   We'll use PUT for simplicity. In real apps, you'd support both.
     *
     * Flow:
     *   1. Find the task by ID
     *   2. If not found → return 404
     *   3. If found → replace it with updated data, keep original createdAt
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request
    ) {
        // Find the index of the task in our list
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id().equals(id)) {
                // Found it! Create an updated version
                TaskResponse updated = new TaskResponse(
                        id,                                  // Keep the same ID
                        request.title(),                     // New title
                        request.description(),               // New description
                        request.status(),                    // New status
                        request.priority(),                  // New priority
                        tasks.get(i).createdAt(),            // Keep original creation time!
                        LocalDateTime.now()                  // Update the "updatedAt" time
                );
                tasks.set(i, updated);  // Replace the old task with the updated one
                return ResponseEntity.ok(updated);
            }
        }

        // Task not found → 404
        return ResponseEntity.notFound().build();
    }

    // ====================================================================
    // DELETE /api/tasks/{id} — Delete a task
    // ====================================================================
    /**
     * @DeleteMapping → handles HTTP DELETE requests
     *
     * Flow:
     *   1. Try to remove the task with matching ID
     *   2. If removed → return 204 NO CONTENT (success, but nothing to return)
     *   3. If not found → return 404 NOT FOUND
     *
     * Why 204 and not 200?
     *   204 NO CONTENT = "I did what you asked, but there's nothing to send back"
     *   It's the standard HTTP code for successful deletions.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // removeIf returns true if it removed something, false if not
        boolean removed = tasks.removeIf(task -> task.id().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();  // 204 — deleted successfully
        } else {
            return ResponseEntity.notFound().build();   // 404 — task didn't exist
        }
    }
}
