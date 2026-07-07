package com.taskforge.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * STANDARD ERROR RESPONSE — A consistent format for ALL errors.
 *
 * Why a standard error format?
 * ----------------------------
 * Without this, different errors return different shapes:
 *   - 404: {"timestamp":"...", "status":404, "error":"Not Found"}  (Spring default)
 *   - 500: {"timestamp":"...", "status":500, "trace":"..."}        (ugly stack trace!)
 *   - 400: {"timestamp":"...", "errors":[...]}                     (different format again!)
 *
 * With this, EVERY error looks the same:
 *   {
 *     "timestamp": "2026-07-07T21:39:00",
 *     "status": 404,
 *     "error": "Not Found",
 *     "message": "Task not found with id: 99",
 *     "path": "/api/tasks/99"
 *   }
 *
 * The frontend team will thank you — they only need ONE error handling pattern.
 *
 * @JsonInclude(NON_NULL) → Don't include fields that are null in the JSON output.
 *   So if "fieldErrors" is null (for non-validation errors), it won't appear in the response.
 *   This keeps the error response clean.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,       // When the error happened
        int status,                    // HTTP status code (400, 404, 500)
        String error,                  // HTTP status name ("Bad Request", "Not Found")
        String message,                // Human-readable description of what went wrong
        String path,                   // The URL that caused the error
        Map<String, String> fieldErrors // Validation errors per field (null if not a validation error)
) {
    /**
     * Factory method for simple errors (no field-level details).
     * Used for 404 Not Found, 500 Internal Server Error, etc.
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null);
    }

    /**
     * Factory method for validation errors (includes per-field details).
     * Used for 400 Bad Request when @Valid fails.
     */
    public static ErrorResponse ofValidation(String message, String path, Map<String, String> fieldErrors) {
        return new ErrorResponse(LocalDateTime.now(), 400, "Bad Request", message, path, fieldErrors);
    }
}
