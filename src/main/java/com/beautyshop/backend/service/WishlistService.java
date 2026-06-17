package com.beautyshop.backend.service;

import com.beautyshop.backend.dto.ProductDTO;
import com.beautyshop.backend.dto.WishlistDTO;
import com.beautyshop.backend.model.*;
import com.beautyshop.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public WishlistDTO getWishlistByUserId(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createWishlist(userId));
        return toDTO(wishlist);
    }

    public WishlistDTO addToWishlist(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createWishlist(userId));

        boolean alreadyExists = wishlist.getProducts().stream()
                .anyMatch(p -> p.getId().equals(productId));

        if (!alreadyExists) {
            wishlist.getProducts().add(product);
            wishlistRepository.save(wishlist);
        }

        return toDTO(wishlist);
    }

    public WishlistDTO removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlist.getProducts().removeIf(p -> p.getId().equals(productId));
        wishlistRepository.save(wishlist);

        return toDTO(wishlist);
    }

    private Wishlist createWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProducts(new ArrayList<>());
        return wishlistRepository.save(wishlist);
    }

    private WishlistDTO toDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());
        dto.setProducts(wishlist.getProducts().stream()
                .map(productService::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }
}
