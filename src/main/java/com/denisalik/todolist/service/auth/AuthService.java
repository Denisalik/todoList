package com.denisalik.todolist.service.auth;

import com.denisalik.todolist.config.jwt.JWTConfiguration;
import com.denisalik.todolist.dto.req.CreateUserRequest;
import com.denisalik.todolist.dto.req.LoginRequest;
import com.denisalik.todolist.dto.req.RegisterRequest;
import com.denisalik.todolist.dto.res.AccessTokenResponse;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.exception.AuthorityNotFoundException;
import com.denisalik.todolist.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JWTService jwtService;
    private JWTConfiguration jwtConfiguration;
    private AuthenticationManager authenticationManager;

    public AccessTokenResponse registration(RegisterRequest req, HttpServletResponse response) {
        log.info("Creating user with username: {}", req.username());
        List<Long> roles = List.of(userService.getAuthorityByName("ROLE_USER").orElseThrow(AuthorityNotFoundException::new).getId());
        CreateUserRequest user = new CreateUserRequest(req.username(), req.email(), req.password(), roles);
        userService.createUser(user);
        //if no error pushed to exception handler then user is created successfully
        return login(new LoginRequest(req.username(), req.password()), response);
    }

    public AccessTokenResponse refresh(String token) {
        log.info("Refreshing access token...");
        String username = jwtService.extractUsername(token);
        userDetailsService.loadUserByUsername(username); // Validate user exists
        log.info("User exists: {}", username);

        String accessToken = jwtService.generateToken(username, "access");
        log.info("Access token refreshed successfully for user: {}", username);

        return new AccessTokenResponse(accessToken);
    }

    public AccessTokenResponse login(LoginRequest req, HttpServletResponse response) {
        log.info("Logging in user with username: {}", req.username());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        String accessToken = jwtService.generateToken(req.username(), "access");
        String refreshToken = jwtService.generateToken(req.username(), "refresh");
        response.addCookie(getRefreshTokenCookie(refreshToken));
        log.info("User {} successfully logged in", req.username());

        return new AccessTokenResponse(accessToken);
    }

    public User me(String token) {
        log.info("Fetching authenticated user details...");
        String username = jwtService.extractUsername(token);
        log.info("Authenticated user: {}", username);

        return (User) userDetailsService.loadUserByUsername(username);

    }

    private Cookie getRefreshTokenCookie(String refreshToken) {
        Cookie c = new Cookie("refreshToken", refreshToken);

        c.setMaxAge(jwtConfiguration.REFRESH_TOKEN_TTL_IN_SECONDS());
        c.setHttpOnly(true);
        c.setPath("/");
        return c;
    }

}
