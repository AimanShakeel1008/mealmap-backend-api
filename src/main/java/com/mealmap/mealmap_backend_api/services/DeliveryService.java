package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;

import java.util.List;

public interface DeliveryService {
    List<DeliveryDto> getAllDeliveriesOfADeliveryPersonnel();

    DeliveryDto updateDeliveryStatusToTheWayToRestaurant(Long deliveryId);

    DeliveryDto getDeliveryByDeliveryId(Long deliveryId);

    DeliveryDto updateDeliveryStatusToAtTheRestaurant(Long deliveryId);

    DeliveryDto updateDeliveryStatusToOrderPickedUp(Long deliveryId);

    DeliveryDto updateDeliveryStatusToOnTheWay(Long deliveryId);

    DeliveryDto updateDeliveryStatusToReachedCustomerLocation(Long deliveryId);

    DeliveryDto updateDeliveryStatusToOrderDelivered(Long deliveryId);
}
