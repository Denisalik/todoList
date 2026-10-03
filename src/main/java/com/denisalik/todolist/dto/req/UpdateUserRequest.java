package com.denisalik.todolist.dto.req;

import jakarta.validation.constraints.Email;

import java.util.List;

public record UpdateUserRequest(
        @Email
        String email,
        List<Long> authorityIds,
        Boolean banned
) {
}
