package com.denisalik.todolist.controller;

import com.denisalik.todolist.dto.req.ToDoItemRequest;
import com.denisalik.todolist.entity.ToDoItem;
import com.denisalik.todolist.exception.ToDoItemNotFoundException;
import com.denisalik.todolist.service.ToDoService;
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

@Tag(name = "Todo List", description = "Todo List management APIs")
@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ToDoController {
    private ToDoService toDoService;

    @Operation(summary = "Get all todo items", description = "Returns a list of all todo items")
    @GetMapping
    public ResponseEntity<List<ToDoItem>> getAll() {
        return ResponseEntity.ok(toDoService.getToDoItems());
    }

    @Operation(summary = "Get todo item by ID", description = "Returns a todo item by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ToDoItem> getById(@PathVariable Long id) {
        return toDoService.getToDoItemById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ToDoItemNotFoundException(id));
    }

    @Operation(summary = "Create a new todo item", description = "Creates a new todo item")
    @PostMapping
    public ResponseEntity<ToDoItem> create(@Valid @RequestBody ToDoItemRequest item) {
        var createdItem = toDoService.createToDoItem(item);
        return ResponseEntity
                .created(URI.create("/api/todos/" + createdItem.getId()))
                .body(createdItem);
    }

    @Operation(summary = "Update a todo item", description = "Updates an existing todo item, fields are non-nullable")
    @PutMapping("/{id}")
    public ResponseEntity<ToDoItem> update(@PathVariable Long id, @Valid @RequestBody ToDoItemRequest item) {
        var updatedItem = toDoService.updateToDoItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Delete a todo item", description = "Deletes an existing todo item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        toDoService.deleteToDoItem(id);
        return ResponseEntity.noContent().build();
    }
}
