package com.denisalik.todolist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false,  unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String hashedPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(nullable = false)
    private Boolean banned;

    @LastModifiedDate
    private Instant updatedDate;

    @CreatedDate
    private Instant createdDate;

    @Version
    private Long version;

    @Override
    public @Nonnull Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public @Nonnull String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public @Nonnull String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    public boolean isAdmin() {
        return this.hasRole("ADMIN");
    }

    public boolean hasRole(String role) {
        return this.hasAuthority("ROLE_" + role);
    }

    public boolean hasAuthority(String authority) {
        return this.roles
                .stream()
                .anyMatch(a -> a
                        .getName()
                        .equals(authority)
                );
    }
}
