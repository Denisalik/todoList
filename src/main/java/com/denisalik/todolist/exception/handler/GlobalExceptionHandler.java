package com.denisalik.todolist.exception.handler;

import com.denisalik.todolist.dto.res.ErrorResponse;
import com.denisalik.todolist.exception.AuthorityNotFoundException;
import com.denisalik.todolist.exception.EmailAlreadyExistsException;
import com.denisalik.todolist.exception.RoleNameAlreadyExistsException;
import com.denisalik.todolist.exception.ToDoItemNotFoundException;
import com.denisalik.todolist.exception.ToDoSuggestionNotFoundException;
import com.denisalik.todolist.exception.UserNotAuthenticatedException;
import com.denisalik.todolist.exception.UserNotFoundException;
import com.denisalik.todolist.exception.UsernameAlreadyExistsException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ApiResponses({
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "405", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public class GlobalExceptionHandler extends CommonGlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
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
    @ExceptionHandler(ToDoSuggestionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleToDoSuggestionNotFoundException(ToDoSuggestionNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(AuthorityNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
