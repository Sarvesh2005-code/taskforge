package com.taskforge.dto.request;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Updated TaskRequest — now includes assigneeId and labelIds
 * for managing relationships via the API.
 */
public record TaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        TaskStatus status,

        @NotNull(message = "Priority is required")
        TaskPriority priority,

        Long assigneeId,         // Optional — which user is assigned (null = unassigned)

        Set<Long> labelIds       // Optional — which labels to attach (null = no labels)
) {
}
