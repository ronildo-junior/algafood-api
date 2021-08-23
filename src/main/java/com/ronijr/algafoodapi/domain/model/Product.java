package com.ronijr.algafoodapi.domain.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Product extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Boolean active;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;
}
