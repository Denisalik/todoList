package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.Size;

public record UpdateToDoItemRequest(
        @Size(min = 1, max = 32, message = "Title must be between 1 and 32 characters")
        String title,
        String description,
        Boolean selected
) {}
