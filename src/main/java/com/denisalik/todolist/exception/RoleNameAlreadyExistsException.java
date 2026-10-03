package com.denisalik.todolist.exception;

public class RoleNameAlreadyExistsException extends RuntimeException {
    public RoleNameAlreadyExistsException() {
        super("Name of Role already exists");
    }

    public RoleNameAlreadyExistsException(String name) {
        super("Name of Role %s already exists".formatted(name));
    }
}
