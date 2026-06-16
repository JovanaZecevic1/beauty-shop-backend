package com.beautyshop.backend.repository;

import com.beautyshop.backend.model.Product;
import com.beautyshop.backend.model.SkinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    Page<Product> findBySkinTypeAndActiveTrue(SkinType skinType, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
    List<Product> findBySkinTypeAndActiveTrue(SkinType skinType);
}
