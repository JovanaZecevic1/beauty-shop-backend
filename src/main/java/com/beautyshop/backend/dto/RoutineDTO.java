package com.beautyshop.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoutineDTO {

    private Long id;
    private String name;
    private List<RoutineItemDTO> items;
}
