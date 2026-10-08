package com.denisalik.todolist.unit;

import com.denisalik.todolist.controller.UserController;
import com.denisalik.todolist.dto.req.CreateUserRequest;
import com.denisalik.todolist.dto.req.UpdateUserRequest;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.exception.UserNotFoundException;
import com.denisalik.todolist.service.UserService;
import com.denisalik.todolist.service.auth.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTService jwtService;

    @Test
    void shouldCreateUser() throws Exception {
        String password = "123";
        String hashedPassword = passwordEncoder.encode(password);
        var request = new CreateUserRequest("Alice", "alice@example.com", password, List.of());
        var response = User.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .hashedPassword(hashedPassword)
                .banned(false)
                .build();

        when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void shouldReturn400WhenEmailInvalid() throws Exception {
        var request = new CreateUserRequest("Alice", "not-an-email", "123", List.of());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void shouldReturn404WhenUserNotFoundUserGetById() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: 99"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        String password = "123";
        String hashedPassword = passwordEncoder.encode(password);
        var response = User.builder()
                .id(1L)
                .username("Alice")
                .email("alice2@example.com")
                .hashedPassword(hashedPassword)
                .banned(false)
                .build();
        when(userService.updateUser(any(UpdateUserRequest.class), eq(1L)))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"alice2@example.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("alice2@example.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundUserUpdate() throws Exception {
        when(userService.updateUser(any(UpdateUserRequest.class), eq(99L)))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ghost","email":"ghost@example.com"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: 99"));
    }

    @Test
    void shouldReturn400WhenBodyInvalid() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","email":"not-an-email"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void shouldReturn400WhenIdIsNotANumber() throws Exception {
        mockMvc.perform(put("/api/users/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@example.com"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}