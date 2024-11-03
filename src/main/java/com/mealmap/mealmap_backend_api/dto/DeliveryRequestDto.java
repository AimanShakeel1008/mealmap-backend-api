package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRequestDto {

    private Long id;

    private CustomerDto customer;

    private RestaurantDto restaurant;

    private DeliveryStatus deliveryStatus;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
