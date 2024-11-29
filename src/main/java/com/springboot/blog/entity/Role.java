package com.springboot.blog.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Fix: Role should reference User, not other roles
    @ManyToMany(mappedBy = "roles")
    @JsonBackReference // Prevent infinite recursion (this side of the bidirectional relationship)
    private Set<User> users = new HashSet<>();
}
