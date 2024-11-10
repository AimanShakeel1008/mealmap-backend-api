package com.mealmap.mealmap_backend_api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {


    private Long id;
    private String name;
    private String address;
    private String hoursOfOperation;
    private String cuisineType;
    private String contactNumber;
    private Boolean open;
    private Boolean active;
}
