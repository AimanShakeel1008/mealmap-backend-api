package com.mealmap.mealmap_backend_api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {

    private Long id;

    private String title;

    private List<MenuItemDto> items;

}
