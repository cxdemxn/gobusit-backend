package com.gobusit.role.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {
    @Id
    private String id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles;
}
