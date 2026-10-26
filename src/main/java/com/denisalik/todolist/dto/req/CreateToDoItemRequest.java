package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateToDoItemRequest(
        @NotBlank(message = "Title is mandatory")
        @Size(min = 1, max = 32, message = "Title must be between 1 and 32 characters")
        String title,
        @NotBlank(message = "Description is mandatory")
        String description
) {}
