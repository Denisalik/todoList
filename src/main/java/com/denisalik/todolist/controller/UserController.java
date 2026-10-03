package com.denisalik.todolist.controller;

import com.denisalik.todolist.dto.req.CreateUserRequest;
import com.denisalik.todolist.dto.req.UpdateUserRequest;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.exception.UserNotFoundException;
import com.denisalik.todolist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "User", description = "User management APIs")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private UserService userService;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService
                .getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest user) {
        var createdUser = userService.createUser(user);
        return ResponseEntity
                .created(URI.create("/api/users/" + createdUser.getId()))
                .body(createdUser);
    }
    @Operation(summary = "Update a user", description = "Updates an existing user, fields can be nullable")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest user) {
        var updatedUser = userService.updateUser(user, id);
        return ResponseEntity.ok(updatedUser);
    }
    @Operation(summary = "Delete a user", description = "Deletes an existing user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
