package com.mealmap.mealmap_backend_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPersonnelDto {

    private Long id;
    private UserDto user;
    private Boolean available;
    private String contactNumber;
}
