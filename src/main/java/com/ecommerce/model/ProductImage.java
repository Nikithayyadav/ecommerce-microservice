package com.ecommerce.model;
import jakarta.persistence.*;import lombok.*;
@Entity @Table(name="product_images") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductImage { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private Product product; @Column(name="image_url",nullable=false) private String imageUrl; @Column(name="is_primary") @Builder.Default private Boolean isPrimary=false; }
