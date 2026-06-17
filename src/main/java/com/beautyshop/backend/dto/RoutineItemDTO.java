package com.beautyshop.backend.dto;

import lombok.Data;

@Data
public class RoutineItemDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer stepOrder;
}
