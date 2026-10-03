package com.denisalik.todolist.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(Long id) {
        super("User %s not found".formatted(id));
    }
}
