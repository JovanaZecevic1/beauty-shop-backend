package com.beautyshop.backend.dto;

import com.beautyshop.backend.model.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String stripePaymentId;
    private String shippingAddress;
    private LocalDateTime createdAt;
}
