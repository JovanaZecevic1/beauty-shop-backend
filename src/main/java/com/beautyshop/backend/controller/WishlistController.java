package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.WishlistDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistDTO> getWishlist(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(user.getId()));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<WishlistDTO> addToWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(user.getId(), productId));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<WishlistDTO> removeFromWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(user.getId(), productId));
    }
}
