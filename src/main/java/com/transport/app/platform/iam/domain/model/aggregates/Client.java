package com.transport.app.platform.iam.domain.model.aggregates;

import com.transport.app.platform.iam.domain.model.entities.Role;
import com.transport.app.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
public class Client extends AuditableAbstractAggregateRoot<Client> {

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Client() { this.roles = new HashSet<>();}

    public Client(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public Client(String username, String password, List<Role> roles) {
        this(username, password);
        addRoles(roles);
    }

    public Client addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public Client addRoles(List<Role> roles) {
        var validatedRoles = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoles);
        return this;
    }
}
