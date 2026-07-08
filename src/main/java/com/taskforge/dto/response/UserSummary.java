package com.taskforge.dto.response;

/**
 * UserSummary — A LIGHTWEIGHT version of the User for embedding in other DTOs.
 *
 * Why not use the full User entity?
 *   1. We'd expose the password field (SECURITY RISK!)
 *   2. The tasks list would cause infinite loops
 *   3. The frontend only needs id, username, email for display
 *
 * This is a common pattern: create a "summary" DTO for nested responses.
 */
public record UserSummary(
        Long id,
        String username,
        String email
) {
}
