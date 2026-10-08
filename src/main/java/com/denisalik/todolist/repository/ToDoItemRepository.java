package com.denisalik.todolist.repository;

import com.denisalik.todolist.entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
    public List<ToDoItem> findAllByCreatedBy(String createdBy);
}
