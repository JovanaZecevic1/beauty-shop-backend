package com.beautyshop.backend.model;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private String imageUrl;

    private String brand;

    @Enumerated(EnumType.STRING)
    private SkinType skinType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private boolean active = true;
}
