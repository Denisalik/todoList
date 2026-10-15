package com.denisalik.todolist.controller;

import com.denisalik.todolist.dto.req.ToDoItemRequest;
import com.denisalik.todolist.entity.ToDoItem;
import com.denisalik.todolist.entity.ToDoSuggestion;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.service.ToDoService;
import com.denisalik.todolist.service.ToDoSuggestionService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

@Tag(name = "Todo List", description = "Todo List management APIs")
@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ToDoController {
    private ToDoService toDoService;
    private ToDoSuggestionService toDoSuggestionService;

    @Operation(summary = "Get all todo items", description = "Returns a list of all todo items created by user, or all of them for admin")
    @GetMapping
    public ResponseEntity<List<ToDoItem>> getAll(@AuthenticationPrincipal User user) {
        List<ToDoItem> items = user.isAdmin() ? toDoService.getToDoItems() : toDoService.getToDoItemsByUser(user.getUsername());
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Get todo item by ID", description = "Returns a todo item by its ID if user created it or admin")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ToDoItem> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(toDoService.getToDoItemByIdOrException(id));
    }

    @Operation(summary = "Create a new todo item", description = "Creates a new todo item")
    @PostMapping
    public ResponseEntity<ToDoItem> create(@Valid @RequestBody ToDoItemRequest item) {
        ToDoItem createdItem = toDoService.createToDoItem(item);
        return ResponseEntity
                .created(URI.create("/api/todos/" + createdItem.getId()))
                .body(createdItem);
    }

    @Operation(summary = "Update a todo item", description = "Updates an existing todo item, fields are non-nullable")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ToDoItem> update(@PathVariable Long id, @Valid @RequestBody ToDoItemRequest item) {
        ToDoItem updatedItem = toDoService.updateToDoItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Delete a todo item", description = "Deletes an existing todo item")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        toDoService.deleteToDoItem(id);
        return ResponseEntity.noContent().build();
    }

    @Timed(value = "todo.suggestion.generate", description = "Time taken to generate AI suggestion")
    @Operation(summary = "Generate or regenerate an AI suggestion for this todo")
    @Parameter(name = "id", description = "ToDo Item ID")
    @PostMapping("/suggestion/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ToDoSuggestion> generateSuggestion(@PathVariable Long id) {
        return ResponseEntity.ok(toDoSuggestionService.generateSuggestion(id));
    }

    @Operation(summary = "Get the existing AI suggestion")
    @Parameter(name = "id", description = "ToDo Item ID")
    @GetMapping("/suggestion/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ToDoSuggestion> getSuggestion(@PathVariable Long id) {
        return ResponseEntity.ok(toDoSuggestionService.getSuggestion(id));
    }
}
