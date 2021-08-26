package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@ToString
public class State extends AbstractEntity<Long>{
    @NotNull(groups = ValidationGroups.ValidateId.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(min = 1, max = 2)
    @Column(nullable = false, length = 2)
    private String abbreviation;
}
