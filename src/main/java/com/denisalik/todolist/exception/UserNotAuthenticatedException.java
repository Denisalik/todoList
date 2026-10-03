package com.denisalik.todolist.exception;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("Missing or invalid Authorization header");
    }
}
