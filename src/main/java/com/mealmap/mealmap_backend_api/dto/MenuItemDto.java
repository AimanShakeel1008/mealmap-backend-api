package com.mealmap.mealmap_backend_api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto {

    private Long id;

    private String name;
    private String description;
    private Double price;
    private Boolean available;
    private Boolean active;
}
