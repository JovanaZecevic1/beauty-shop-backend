package com.beautyshop.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {

    private Long id;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private Long productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
