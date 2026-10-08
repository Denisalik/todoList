package com.denisalik.todolist.service.auth;

import com.denisalik.todolist.config.jwt.JWTConfiguration;
import com.denisalik.todolist.dto.req.CreateUserRequest;
import com.denisalik.todolist.dto.req.LoginRequest;
import com.denisalik.todolist.dto.req.RegisterRequest;
import com.denisalik.todolist.dto.res.AccessTokenResponse;
import com.denisalik.todolist.entity.Role;
import com.denisalik.todolist.exception.AuthorityNotFoundException;
import com.denisalik.todolist.exception.UserNotAuthenticatedException;
import com.denisalik.todolist.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        List<Long> roles = userService
                .getAuthorityByName("ROLE_USER")
                .map(Role::getId)
                .map(List::of)
                .orElseThrow(AuthorityNotFoundException::new);
        CreateUserRequest user = new CreateUserRequest(req.username(), req.email(), req.password(), roles);
        userService.createUser(user);
        //if no error pushed to exception handler then user is created successfully
        return login(new LoginRequest(req.username(), req.password()), response);
    }

    public AccessTokenResponse refresh(String token) {
        log.info("Refreshing access token...");
        if (jwtService.isTokenExpired(token) || !jwtService.extractTokenType(token).equals("refresh")) {
            throw new UserNotAuthenticatedException();
        }
        String username = jwtService.extractUsername(token);
        userDetailsService.loadUserByUsername(username); // Validate user exists

        String accessToken = jwtService.generateToken(username, "access");
        log.info("Access token refreshed successfully for user: {}", username);

        return new AccessTokenResponse(accessToken);
    }

    public AccessTokenResponse login(LoginRequest req, HttpServletResponse response) {
        log.info("Logging in user with username: {}", req.username());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        String accessToken = jwtService.generateToken(req.username(), "access");
        String refreshToken = jwtService.generateToken(req.username(), "refresh");
        response.addHeader(HttpHeaders.SET_COOKIE, getRefreshTokenCookie(refreshToken).toString());
        log.info("User {} successfully logged in", req.username());

        return new AccessTokenResponse(accessToken);
    }

    private ResponseCookie getRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(Duration.ofSeconds(jwtConfiguration.REFRESH_TOKEN_TTL_IN_SECONDS()))
                .build();
    }

}
