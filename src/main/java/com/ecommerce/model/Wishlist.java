package com.ecommerce.model;
import jakarta.persistence.*;import lombok.*;import org.springframework.data.annotation.CreatedDate;import org.springframework.data.jpa.domain.support.AuditingEntityListener;import java.time.LocalDateTime;
@Entity @Table(name="wishlists",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","product_id"})) @Data @Builder @NoArgsConstructor @AllArgsConstructor @EntityListeners(AuditingEntityListener.class)
public class Wishlist { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(name="user_id",nullable=false) private Long userId; @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="product_id",nullable=false) private Product product; @CreatedDate @Column(name="added_at",updatable=false) private LocalDateTime addedAt; }
