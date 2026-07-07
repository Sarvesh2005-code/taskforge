package com.taskforge.exception;

import com.taskforge.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GLOBAL EXCEPTION HANDLER — Catches ALL exceptions across ALL controllers.
 * ==========================================================================
 *
 * WITHOUT this class:
 *   - ResourceNotFoundException → 500 Internal Server Error + ugly stack trace
 *   - Validation failures → Spring's default error format (inconsistent)
 *   - Any unhandled exception → full stack trace leaked to the client (SECURITY RISK!)
 *
 * WITH this class:
 *   - ResourceNotFoundException → 404 Not Found + clean JSON
 *   - Validation failures → 400 Bad Request + per-field error messages
 *   - Any unhandled exception → 500 Internal Server Error + safe message (no stack trace)
 *
 * HOW IT WORKS:
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *   - @ControllerAdvice → "I apply to ALL controllers in the application"
 *   - @ResponseBody     → "Return JSON, not HTML"
 *
 * @ExceptionHandler(SomeException.class)
 *   → "When SomeException is thrown anywhere, run THIS method instead of crashing"
 *
 * Think of it like a safety net under a trapeze:
 *   - If the acrobat (controller) falls (throws an exception)
 *   - The safety net (exception handler) catches them
 *   - And handles the situation gracefully (returns a clean error response)
 *
 * Spring looks for the MOST SPECIFIC handler first:
 *   1. ResourceNotFoundException → handleResourceNotFound()
 *   2. MethodArgumentNotValidException → handleValidationErrors()
 *   3. Any other Exception → handleGenericException()  (catch-all)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ====================================================================
    // HANDLER 1: Resource Not Found (404)
    // ====================================================================
    /**
     * Catches ResourceNotFoundException thrown by our Service layer.
     *
     * Before Module 4:  GET /api/tasks/999 → 500 Internal Server Error + stack trace
     * After Module 4:   GET /api/tasks/999 → 404 Not Found + clean message
     *
     * HttpServletRequest gives us access to the original request URL.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),         // 404
                HttpStatus.NOT_FOUND.getReasonPhrase(), // "Not Found"
                ex.getMessage(),                       // "Task not found with id: 99"
                request.getRequestURI()                // "/api/tasks/99"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ====================================================================
    // HANDLER 2: Validation Errors (400)
    // ====================================================================
    /**
     * Catches MethodArgumentNotValidException — thrown when @Valid fails.
     *
     * When a client sends invalid data:
     *   POST /api/tasks  { "title": "", "priority": null }
     *
     * Spring validates the @RequestBody, finds violations, and throws
     * MethodArgumentNotValidException BEFORE your controller method runs.
     *
     * We extract each field's error and return them in a structured format:
     *   {
     *     "status": 400,
     *     "message": "Validation failed",
     *     "fieldErrors": {
     *       "title": "Title is required",
     *       "priority": "Priority is required"
     *     }
     *   }
     *
     * getBindingResult() → contains all validation errors
     * getFieldErrors()   → list of field-level violations
     * getField()         → which field failed (e.g., "title")
     * getDefaultMessage() → the error message from the annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // Collect all field errors into a Map: { "fieldName": "error message" }
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ErrorResponse error = ErrorResponse.ofValidation(
                "Validation failed — check the fieldErrors for details",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ====================================================================
    // HANDLER 3: Catch-All for Unexpected Errors (500)
    // ====================================================================
    /**
     * Catches ANY unhandled exception — the safety net.
     *
     * IMPORTANT: We never send the stack trace to the client!
     *   - Stack traces expose internal code structure (security risk)
     *   - They're confusing for frontend developers
     *   - We log them on the server instead
     *
     * In production, you'd also:
     *   - Send alerts to Slack/PagerDuty
     *   - Log to a centralized system (ELK, Datadog)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        // Log the full error on the server (for debugging)
        System.err.println("❌ Unhandled exception at " + request.getRequestURI());
        ex.printStackTrace();

        // Send a safe, generic message to the client
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),          // 500
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), // "Internal Server Error"
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
