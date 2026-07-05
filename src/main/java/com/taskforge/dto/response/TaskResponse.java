package com.taskforge.dto.response;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * TaskResponse = what WE SEND BACK to the client.
 *
 * Notice it has MORE fields than TaskRequest:
 *   - "id"        → auto-generated, client doesn't set this
 *   - "createdAt" → auto-generated, client doesn't set this
 *   - "updatedAt" → auto-generated, client doesn't set this
 *
 * This is WHY we use separate DTOs for request vs response:
 *   - TaskRequest:  what the client CAN control (title, description, status, priority)
 *   - TaskResponse: what we SEND BACK (everything, including auto-generated fields)
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
