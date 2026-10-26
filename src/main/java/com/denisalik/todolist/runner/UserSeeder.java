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
import java.util.stream.Collectors;

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
        log.info("*".repeat(50));
        log.info("User Seeder started.");
        var userRole = createRoleIfNotExists("ROLE_USER");
        var adminRole = createRoleIfNotExists("ROLE_ADMIN");

        var user = createUserIfNotExists("user", "user@example.com", "user_password", Set.of(userRole));
        var admin = createUserIfNotExists("admin", "admin@example.com", "admin_password",  Set.of(userRole, adminRole));

        log.info("User Seeder completed.");
        log.info("*".repeat(50));
    }


    private Role createRoleIfNotExists(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = Role.builder()
                    .name(name)
                    .build();
            log.info("Added Role with name:{}", name);
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
            String rolesNames = roles
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
            log.info("Added User with username:{}, password: {}, roles:{}", username, password, rolesNames);
            return userRepository.save(user);
        });
    }

}
