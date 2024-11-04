package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;

import java.util.List;

public interface DeliveryRequestService {
    List<DeliveryRequestDto> getAllDeliveryRequests();

    DeliveryDto acceptDeliveryRequest(Long deliveryRequestId);
}
