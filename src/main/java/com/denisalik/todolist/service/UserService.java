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
    public void addAuthorities(User user, List<Long> authorityIds) {
        authorityIds.forEach(id -> addAuthority(user, id));
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

    public User createUser(CreateUserRequest createUserRequest) {
        log.info("Creating User {}", createUserRequest.username());
        checkUserIntegrity(createUserRequest.username(), createUserRequest.email());
        String hashedPassword = passwordEncoder.encode(createUserRequest.password());
        User user = userRepository.save(User.builder()
                .username(createUserRequest.username())
                .email(createUserRequest.email())
                .hashedPassword(hashedPassword)
                .banned(false)
                .build()
        );
        log.info("Successfully created User {}", user.getUsername());
        addAuthorities(user, createUserRequest.authorityIds());
        return userRepository.save(user);
    }

    public User updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        log.info("Updating User by id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        checkUserIntegrity(user.getUsername(), user.getEmail());

        Optional.ofNullable(updateUserRequest.email())
                .ifPresent(user::setEmail);
        Optional.ofNullable(updateUserRequest.banned())
                .ifPresent(user::setBanned);
        Optional.ofNullable(updateUserRequest.password())
                .map(passwordEncoder::encode)
                .ifPresent(user::setHashedPassword);

        addAuthorities(user, updateUserRequest.authorityIds());
        log.info("Successfully updated User {}", user.getUsername());
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        log.info("Deleting User by id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        log.info("Successfully deleted User {}", user.getUsername());
    }
}
