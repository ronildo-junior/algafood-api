package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class User extends AbstractEntity<Long> {
    @NotNull(groups = ValidationGroups.ValidateId.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "user_group_user",
        joinColumns = @JoinColumn(name ="user_id"),
        inverseJoinColumns = @JoinColumn(name = "user_group_id")
    )
    private final Set<UserGroup> userGroups = new HashSet<>();

    public void updatePassword(String password) {
        this.setPassword(password);
    }

    public boolean passwordEquals(String password) {
        return this.getPassword().equals(password);
    }

    public Optional<UserGroup> getUserGroup(Long id) {
        return userGroups.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public void linkUserGroup(UserGroup userGroup) {
        userGroups.add(userGroup);
    }

    public void unlinkUserGroup(UserGroup userGroup) {
        userGroups.remove(userGroup);
    }
}