package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.OrderDTO;
import com.beautyshop.backend.model.OrderStatus;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(
            @AuthenticationPrincipal User user,
            @RequestParam String shippingAddress) {
        return ResponseEntity.ok(orderService.createOrderFromCart(user.getId(), shippingAddress));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
