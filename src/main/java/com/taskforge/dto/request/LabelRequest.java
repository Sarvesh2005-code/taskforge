package com.taskforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LabelRequest(
        @NotBlank(message = "Label name is required")
        @Size(max = 50, message = "Label name cannot exceed 50 characters")
        String name,

        @NotBlank(message = "Color is required")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex code (e.g., #FF5733)")
        String color
) {
}
