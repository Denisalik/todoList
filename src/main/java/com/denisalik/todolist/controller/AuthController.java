package com.denisalik.todolist.controller;

import com.denisalik.todolist.dto.req.LoginRequest;
import com.denisalik.todolist.dto.req.RegisterRequest;
import com.denisalik.todolist.dto.res.AccessTokenResponse;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.exception.UserNotAuthenticatedException;
import com.denisalik.todolist.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Tag(name = "Auth", description = "Authentication APIs")
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a new user using username, email and password")
    @PostMapping("/register")
    public ResponseEntity<AccessTokenResponse> registration(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        return ResponseEntity
                .created(URI.create("/auth/register"))
                .body(authService.registration(registerRequest, response));
    }

    @Operation(summary = "Refresh access token", description = "Refreshes the access token using refresh token")
    @Parameter(name = "refreshToken", description = "Refresh token", required = true, allowEmptyValue = false)
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UserNotAuthenticatedException();
        }
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @Operation(summary = "Login", description = "Logs in a user using username and password")
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity
                .created(URI.create("/auth/login"))
                .body(authService.login(loginRequest, response));
    }

    @Operation(summary = "Get current user", description = "Returns the current logged-in user(using access token)")
    @GetMapping("/me")
    public ResponseEntity<User> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get CSRF token", description = "Returns the CSRF token for the current session")
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf(CsrfToken token) {
        token.getToken(); // This line is just to ensure the token is generated and sent in the response headers
        return ResponseEntity.noContent().build();
    }

}
