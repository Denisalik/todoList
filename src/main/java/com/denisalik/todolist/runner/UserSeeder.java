package com.denisalik.todolist.runner;

import com.denisalik.todolist.entity.Role;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.repository.RoleRepository;
import com.denisalik.todolist.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {
        var userRole = createRoleIfNotExists("ROLE_USER");
        var adminRole = createRoleIfNotExists("ROLE_ADMIN");

        var user = createUserIfNotExists("user", "user@example.com", "password", Set.of(userRole));
        var admin = createUserIfNotExists("admin", "admin@example.com", "admin",  Set.of(userRole, adminRole));

        log.info("User Seeder completed. User: {}, Admin: {}", user.getUsername(), admin.getUsername());
    }


    private Role createRoleIfNotExists(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = Role.builder()
                    .name(name)
                    .build();
            return roleRepository.save(role);
        });
    }
    private User createUserIfNotExists(String username, String email, String password, Set<Role> roles) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .hashedPassword(passwordEncoder.encode(password))
                    .banned(false)
                    .roles(roles)
                    .build();
            return userRepository.save(user);
        });
    }

}
