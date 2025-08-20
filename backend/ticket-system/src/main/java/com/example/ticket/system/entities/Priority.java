package com.example.ticket.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "priorities")
@Getter
@Setter
@NoArgsConstructor
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "level", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int level; // 1..4 per migration

    public Long getId() {
        return this.id;
    }
}
