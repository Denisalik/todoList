package com.denisalik.todolist.exception.handler;

import com.denisalik.todolist.dto.res.ErrorResponse;
import com.denisalik.todolist.exception.EmailAlreadyExistsException;
import com.denisalik.todolist.exception.RoleNameAlreadyExistsException;
import com.denisalik.todolist.exception.ToDoItemNotFoundException;
import com.denisalik.todolist.exception.UserNotAuthenticatedException;
import com.denisalik.todolist.exception.UserNotFoundException;
import com.denisalik.todolist.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends CommonGlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(RoleNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoleNameExists(RoleNameAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleHeaderIsMalformed(UserNotAuthenticatedException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    @ExceptionHandler(ToDoItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleToDoNotFoundException(ToDoItemNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
