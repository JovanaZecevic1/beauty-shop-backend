package com.beautyshop.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class WishlistDTO {

    private Long id;
    private List<ProductDTO> products;
}
