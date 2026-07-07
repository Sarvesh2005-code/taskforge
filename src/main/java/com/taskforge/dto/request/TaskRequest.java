package com.taskforge.dto.request;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * TaskRequest — NOW WITH VALIDATION!
 * ====================================
 *
 * WHAT IS BEAN VALIDATION?
 * Bean Validation (Jakarta Validation) lets you declare RULES on your fields
 * using annotations. When a request comes in, Spring checks these rules
 * BEFORE your code even runs.
 *
 * If validation fails → Spring automatically returns 400 BAD REQUEST.
 * Your controller method never executes. The invalid data never reaches your service.
 *
 * Think of it like a bouncer at a club:
 *   - The bouncer (validation) checks your ID BEFORE you enter
 *   - If your ID is fake → you're turned away at the door
 *   - The bartender (controller) never sees you
 *
 * HOW IT WORKS:
 *   1. You put validation annotations on the DTO fields
 *   2. You add @Valid on the @RequestBody in the controller
 *   3. Spring validates automatically before calling your method
 *   4. If invalid → throws MethodArgumentNotValidException
 *   5. Our GlobalExceptionHandler catches it → returns clean error JSON
 *
 * COMMON VALIDATION ANNOTATIONS:
 *   @NotNull     → field cannot be null
 *   @NotBlank    → field cannot be null, empty, or just whitespace (Strings only)
 *   @NotEmpty    → field cannot be null or empty (Strings, Collections)
 *   @Size        → min/max length for Strings, min/max size for Collections
 *   @Min / @Max  → minimum/maximum numeric value
 *   @Email       → must be a valid email format
 *   @Pattern     → must match a regex pattern
 *   @Past/@Future → date must be in past/future
 *
 * The "message" attribute is the error message returned when validation fails.
 */
public record TaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,     // optional — no @NotNull, so null is allowed

        TaskStatus status,      // optional — defaults to TODO in the service

        @NotNull(message = "Priority is required")
        TaskPriority priority
) {
}
