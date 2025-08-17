package com.example.ticket.system.entities;

import com.example.ticket.system.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "password_hash", nullable = false)
    protected String password;

    @Column(name = "name", nullable = false)
    protected String name;

    // mapped to discriminator column; keep read-only for JPA, expose via custom methods
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "role", insertable = false, updatable = false)
    protected String role;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "department")
    protected String department;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = false, updatable = false, nullable = false)
    protected Date created_at;

    // Relationships
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    // Expose role as enum as per spec
    public abstract Role getRole();

    public void setRole(Role role) {
        // Note: this writes the field, but JPA will not persist due to insertable/updatable=false.
        // The effective type is controlled by the discriminator via subclass type.
        this.role = role != null ? role.name() : null;
    }
}
