package com.beautyshop.backend.service;

import com.beautyshop.backend.dto.UserDTO;
import com.beautyshop.backend.dto.SkinQuizRequest;
import com.beautyshop.backend.dto.ProductDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.repository.UserRepository;
import com.beautyshop.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        return toDTO(userRepository.save(user));
    }

    public UserDTO saveSkinType(Long userId, SkinQuizRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setSkinType(request.getSkinType());
        return toDTO(userRepository.save(user));
    }

    public List<ProductDTO> getRecommendedProducts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getSkinType() == null) {
            return List.of();
        }

        return productRepository.findBySkinTypeAndActiveTrue(user.getSkinType())
                .stream()
                .map(productService::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setSkinType(user.getSkinType());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
