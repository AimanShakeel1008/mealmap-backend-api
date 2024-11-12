package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long id;
    private User user;
    private String contactNumber;
}
