package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.CartDTO;
import com.beautyshop.backend.dto.RoutineDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.CartService;
import com.beautyshop.backend.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RoutineController {

    private final RoutineService routineService;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getMyRoutines(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(routineService.getRoutinesByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<RoutineDTO> createRoutine(
            @AuthenticationPrincipal User user,
            @RequestParam String name) {
        return ResponseEntity.ok(routineService.createRoutine(user.getId(), name));
    }

    @PostMapping("/{routineId}/add-product")
    public ResponseEntity<RoutineDTO> addProductToRoutine(
            @PathVariable Long routineId,
            @RequestParam Long productId,
            @RequestParam Integer stepOrder) {
        return ResponseEntity.ok(routineService.addProductToRoutine(routineId, productId, stepOrder));
    }

    @DeleteMapping("/{routineId}/remove-product/{routineItemId}")
    public ResponseEntity<RoutineDTO> removeProductFromRoutine(
            @PathVariable Long routineId,
            @PathVariable Long routineItemId) {
        return ResponseEntity.ok(routineService.removeProductFromRoutine(routineId, routineItemId));
    }

    @PostMapping("/{routineId}/add-to-cart")
    public ResponseEntity<CartDTO> addRoutineToCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long routineId) {
        routineService.addRoutineToCart(user.getId(), routineId);
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{routineId}")
    public ResponseEntity<RoutineDTO> updateRoutine(
            @PathVariable Long routineId,
            @RequestParam String name) {
        return ResponseEntity.ok(routineService.updateRoutine(routineId, name));
    }
}
