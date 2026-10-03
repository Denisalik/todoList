package com.denisalik.todolist.service;

import com.denisalik.todolist.dto.req.CreateUserRequest;
import com.denisalik.todolist.dto.req.UpdateUserRequest;
import com.denisalik.todolist.entity.Role;
import com.denisalik.todolist.entity.User;
import com.denisalik.todolist.exception.AuthorityNotFoundException;
import com.denisalik.todolist.exception.EmailAlreadyExistsException;
import com.denisalik.todolist.exception.RoleNameAlreadyExistsException;
import com.denisalik.todolist.exception.UserNotFoundException;
import com.denisalik.todolist.exception.UsernameAlreadyExistsException;
import com.denisalik.todolist.repository.RoleRepository;
import com.denisalik.todolist.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        log.info("Getting All Roles");
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> getAuthorityByName(String name) {
        log.info("Getting Authority {}", name);
        return roleRepository.findByName(name);
    }

    private void checkRoleIntegrity(String name) {
        if (roleRepository.existsByName(name)) {
            throw new RoleNameAlreadyExistsException(name);
        }
    }

    public Role createAuthority(Role authority) {
        log.info("Creating Authority {}", authority.getName());
        checkRoleIntegrity(authority.getName());
        return roleRepository.save(authority);
    }

    public void addAuthority(User user, Long authorityId) {
        log.info("Adding Authority {} to user {}", authorityId,  user.getUsername());
        Role authority = roleRepository.findById(authorityId).orElseThrow(() -> new AuthorityNotFoundException(authorityId));
        user.getRoles().add(authority);
        log.info("Successfully added Authority {} to user {}", authority.getName(),  user.getUsername());
    }


    @Transactional(readOnly = true)
    public List<User> getUsers() {
        log.info("Getting All Users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("Getting User by id {}", id);
        return userRepository.findById(id);
    }

    private void checkUserIntegrity(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public User createUser(CreateUserRequest user) {
        log.info("Creating User {}", user.username());
        checkUserIntegrity(user.username(), user.email());
        String hashedPassword = passwordEncoder.encode(user.password());
        User createdUser = User.builder()
                .username(user.username())
                .email(user.email())
                .hashedPassword(hashedPassword)
                .banned(false)
                .build();
        user.authorityIds().forEach(id -> addAuthority(createdUser, id));

        User saved = userRepository.save(createdUser);
        log.info("Successfully created User {}", saved.getUsername());
        return saved;
    }

    public User updateUser(UpdateUserRequest user, Long userId) {
        log.info("Updating User by id {}", userId);
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        checkUserIntegrity(oldUser.getUsername(), oldUser.getEmail());

        Optional.ofNullable(user.email()).ifPresent(oldUser::setEmail);
        Optional.ofNullable(user.banned()).ifPresent(oldUser::setBanned);
        user.authorityIds().forEach(id -> addAuthority(oldUser, id));

        User saved = userRepository.save(oldUser);
        log.info("Successfully updated User {}", saved.getUsername());
        return saved;
    }
    public void deleteUser(Long id) {
        log.info("Deleting User by id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        log.info("Successfully deleted User {}", user.getUsername());
    }
}
