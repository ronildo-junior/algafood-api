package com.ronijr.algafoodapi.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class ProductPhoto extends AbstractEntity<Long> {
    @Id
    @Column(name = "product_id")
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    private String fileName;
    private String description;
    private String contentType;
    private Long size;
}