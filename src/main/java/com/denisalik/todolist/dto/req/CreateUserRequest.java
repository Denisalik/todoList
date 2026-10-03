package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "Username is mandatory")
        String username,
        @Email
        @NotNull
        String email,
        @NotBlank(message = "Password is mandatory")
        String password,
        List<Long> authorityIds
) {}
