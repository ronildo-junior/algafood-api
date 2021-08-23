package com.ronijr.algafoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class User extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "user_group_user",
        joinColumns = @JoinColumn(name ="user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<UserGroup> groups = new ArrayList<>();
}
