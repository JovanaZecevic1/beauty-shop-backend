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

    @Column(name = "image_url_2")
    private String imageUrl2;

    @Column(name = "image_url_3")
    private String imageUrl3;

    @Column(name = "sales_count", nullable = false)
    private Integer salesCount = 0;

    private String brand;

    @Enumerated(EnumType.STRING)
    private SkinType skinType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private boolean active = true;


}