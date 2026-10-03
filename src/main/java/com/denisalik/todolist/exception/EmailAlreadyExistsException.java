package com.denisalik.todolist.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
    public EmailAlreadyExistsException(String email) {
        super("Email %s already exists".formatted(email));
    }
}
