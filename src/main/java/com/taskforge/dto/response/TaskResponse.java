package com.taskforge.dto.response;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Updated TaskResponse — now includes assignee info and labels.
 *
 * Notice: we use UserSummary (not the full User) and LabelResponse
 * (not the full Label entity). This avoids:
 *   1. Exposing passwords
 *   2. Infinite loops (User → Tasks → User → Tasks → ...)
 *   3. Sending unnecessary data to the frontend
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UserSummary assignee,           // Nested user info (or null if unassigned)
        Set<LabelResponse> labels,      // Nested label list (empty set if no labels)
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
