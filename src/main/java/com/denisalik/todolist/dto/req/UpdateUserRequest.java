package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserRequest(
        @Email
        @Size(min = 6, max = 32, message = "Email must be between 6 and 32 characters")
        String email,
        List<Long> authorityIds,
        Boolean banned,
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
        String password
) {
}
