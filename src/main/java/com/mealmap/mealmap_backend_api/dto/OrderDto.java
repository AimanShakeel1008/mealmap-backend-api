package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private CustomerDto customer;
    private RestaurantDto restaurant;
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal itemTotal;
    private BigDecimal taxes;
    private BigDecimal deliveryCharges;
    private BigDecimal totalAmount;
    private PaymentMode paymentMode;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
