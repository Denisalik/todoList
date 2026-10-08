package com.denisalik.todolist.controller.security;

import com.denisalik.todolist.repository.ToDoItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("toDoSecurity")
@AllArgsConstructor
public class ToDoSecurity {
    private final ToDoItemRepository repo;
    public boolean isOwner(Long id, String username) {
        return repo.findById(id)
                .map(i -> i.getCreatedBy().equals(username))
                .orElse(false);
    }
}
