package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RegisterRequest(
        @NotBlank(message = "Username is mandatory")
        String username,
        @Email
        @NotNull
        String email,
        @NotBlank(message = "Password is mandatory")
        String password
) {}
