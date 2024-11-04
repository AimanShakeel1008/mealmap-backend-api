package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
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
public class DeliveryDto {

    private Long id;
    private OrderDto order;
    private DeliveryPersonnelDto deliveryPersonnel;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
