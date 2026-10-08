package com.denisalik.todolist.dto.res;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        Instant timestamp,
        Map<String, String> errors  // field-level validation errors
) {}
