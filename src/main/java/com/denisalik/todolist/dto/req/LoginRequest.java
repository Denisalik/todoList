package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 4, max = 32, message = "Username must be between 4 and 32 characters")
        String username,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
        String password
) {}
