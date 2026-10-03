package com.denisalik.todolist.exception;

public class ToDoItemNotFoundException extends RuntimeException {
    public ToDoItemNotFoundException(Long id) {
        super("ToDo item %s not found".formatted(id));
    }
    public ToDoItemNotFoundException() {
        super("ToDo item not found");
    }
}
