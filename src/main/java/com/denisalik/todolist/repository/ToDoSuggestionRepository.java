package com.denisalik.todolist.repository;

import com.denisalik.todolist.entity.ToDoSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToDoSuggestionRepository extends JpaRepository<ToDoSuggestion, Long> {
    Optional<ToDoSuggestion> findByToDoItemId(Long toDoItemId);
}
