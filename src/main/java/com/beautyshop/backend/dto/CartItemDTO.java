package com.beautyshop.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal total;
}
