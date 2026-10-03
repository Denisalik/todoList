package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ToDoItemRequest(
        @NotBlank(message = "Title is mandatory")
        String title,
        @NotBlank(message = "Description is mandatory")
        String description
) {}
