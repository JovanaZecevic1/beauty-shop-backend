package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.CartDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addToCart(user.getId(), productId, quantity));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(user.getId(), cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(user.getId(), cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok().build();
    }
}
