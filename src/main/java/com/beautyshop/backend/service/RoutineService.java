package com.beautyshop.backend.service;

import com.beautyshop.backend.dto.RoutineDTO;
import com.beautyshop.backend.dto.RoutineItemDTO;
import com.beautyshop.backend.model.*;
import com.beautyshop.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineItemRepository routineItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public List<RoutineDTO> getRoutinesByUserId(Long userId) {
        return routineRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RoutineDTO createRoutine(Long userId, String name, String routineType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = new Routine();
        routine.setUser(user);
        routine.setName(name);
        routine.setRoutineType(routineType != null ? routineType : "Morning");
        return toDTO(routineRepository.save(routine));
    }

    public RoutineDTO addProductToRoutine(Long routineId, Long productId, Integer stepOrder) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        RoutineItem item = new RoutineItem();
        item.setRoutine(routine);
        item.setProduct(product);
        item.setStepOrder(stepOrder);

        routineItemRepository.save(item);

        return toDTO(routineRepository.findById(routineId).get());
    }

    public RoutineDTO removeProductFromRoutine(Long routineId, Long routineItemId) {
        routineItemRepository.deleteById(routineItemId);
        return toDTO(routineRepository.findById(routineId).get());
    }

    public void deleteRoutine(Long routineId) {
        routineRepository.deleteById(routineId);
    }

    public void addRoutineToCart(Long userId, Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        routine.getItems().forEach(item -> {
            cartService.addToCart(userId, item.getProduct().getId(), 1);
        });
    }

    public RoutineDTO updateRoutine(Long routineId, String name, String routineType) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        routine.setName(name);
        if (routineType != null) routine.setRoutineType(routineType);
        routine.getItems().clear();
        return toDTO(routineRepository.save(routine));
    }

    private RoutineDTO toDTO(Routine routine) {
        RoutineDTO dto = new RoutineDTO();
        dto.setId(routine.getId());
        dto.setName(routine.getName());
        dto.setRoutineType(routine.getRoutineType());

        List<RoutineItemDTO> items = routine.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }

    private RoutineItemDTO toItemDTO(RoutineItem item) {
        RoutineItemDTO dto = new RoutineItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImage(item.getProduct().getImageUrl());
        dto.setStepOrder(item.getStepOrder());
        return dto;
    }
}