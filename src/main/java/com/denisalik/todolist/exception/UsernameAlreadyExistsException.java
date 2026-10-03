package com.denisalik.todolist.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
    public UsernameAlreadyExistsException(String username) {
        super("Username %s already exists".formatted(username));
    }
}
