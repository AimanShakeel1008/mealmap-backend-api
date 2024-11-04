package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.OrderDto;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;

public interface OrderService {
    OrderDto placeOrder(PaymentMode paymentMode);

    OrderDto updateOrderStatus(Long orderId);
}
