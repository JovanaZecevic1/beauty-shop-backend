package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.ReviewDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @AuthenticationPrincipal User user,
            @RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.createReview(user.getId(), dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(user.getId(), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        reviewService.deleteReview(user.getId(), id);
        return ResponseEntity.ok().build();
    }
}
