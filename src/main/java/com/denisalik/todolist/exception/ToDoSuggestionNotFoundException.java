package com.denisalik.todolist.exception;

public class ToDoSuggestionNotFoundException extends RuntimeException {
    public ToDoSuggestionNotFoundException(Long toDoItemId) {
        super("ToDo suggestion not found for id: %s".formatted(toDoItemId));
    }
    public ToDoSuggestionNotFoundException() {
        super("ToDo suggestion not found");
    }
}
