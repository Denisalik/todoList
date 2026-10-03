package com.denisalik.todolist.exception;

public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException() {
        super("Authority not found");
    }
    public AuthorityNotFoundException(Long id) {
        super("Authority %s not found".formatted(id));
    }
}
