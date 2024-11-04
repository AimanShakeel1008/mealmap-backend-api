package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;

import java.util.List;

public interface DeliveryService {
    List<DeliveryDto> getAllDeliveries();

    DeliveryDto updateDeliveryStatus(Long deliveryId, DeliveryStatus deliveryStatus);
}
