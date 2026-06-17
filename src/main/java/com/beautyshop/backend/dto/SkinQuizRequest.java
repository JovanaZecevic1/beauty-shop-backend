package com.beautyshop.backend.dto;

import com.beautyshop.backend.model.SkinType;
import lombok.Data;

@Data
public class SkinQuizRequest {

    private SkinType skinType;
}
