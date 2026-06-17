package com.beautyshop.backend.dto;

import com.beautyshop.backend.model.Role;
import com.beautyshop.backend.model.SkinType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private SkinType skinType;
    private LocalDateTime createdAt;
}
