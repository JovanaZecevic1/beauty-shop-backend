package com.beautyshop.backend.dto;

import com.beautyshop.backend.model.SkinType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private String brand;
    private SkinType skinType;
    private Long categoryId;
    private String categoryName;
    private boolean active;
}
