package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;
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

    private OrderDto order;

    private DeliveryRequestStatus deliveryRequestStatus;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
