package com.denisalik.todolist.controller.security;

import com.denisalik.todolist.repository.ToDoItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("toDoSecurity")
@AllArgsConstructor
public class ToDoSecurity {
    private final ToDoItemRepository repo;
    public boolean isOwner(Long id, String username) {
        log.info("authorization using toDoSecurity helper for username:{}", username);
        return repo.findById(id)
                .map(i -> i.getCreatedBy().equals(username))
                .orElse(false);
    }
}
