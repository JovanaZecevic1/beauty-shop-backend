package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.ProductDTO;
import com.beautyshop.backend.dto.SkinQuizRequest;
import com.beautyshop.backend.dto.UserDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(user.getId(), dto));
    }

    @PostMapping("/skin-quiz")
    public ResponseEntity<UserDTO> saveSkinType(
            @AuthenticationPrincipal User user,
            @RequestBody SkinQuizRequest request) {
        return ResponseEntity.ok(userService.saveSkinType(user.getId(), request));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<ProductDTO>> getRecommendations(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getRecommendedProducts(user.getId()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
