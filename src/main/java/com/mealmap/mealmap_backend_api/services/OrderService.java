package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.OrderDto;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(PaymentMode paymentMode);

    OrderDto acceptOrder(Long orderId, Long restaurantId);

    OrderDto updateStatusToInProgress(Long orderId, Long restaurantId);

    OrderDto updateStatusToReceivedByDeliveryPersonnel(Long orderId, Long restaurantId);

    OrderDto updateStatusToOrderReady(Long orderId, Long restaurantId);

    List<OrderDto> getAllOrdersOfARestaurant(Long restaurantId);

    OrderDto getOrderOfARestaurantByOrderId(Long restaurantId, Long orderId);

    List<OrderDto> getAllOrdersOfACustomer();

    OrderDto getOrdersOfACustomerByOrderId(Long orderId);
}
