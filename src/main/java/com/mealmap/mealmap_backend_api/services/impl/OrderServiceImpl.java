package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.OrderDto;
import com.mealmap.mealmap_backend_api.entities.Cart;
import com.mealmap.mealmap_backend_api.entities.Order;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentStatus;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CartRepository;
import com.mealmap.mealmap_backend_api.respositories.OrderRepository;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto placeOrder(PaymentMode paymentMode) {
        Cart cart = cartRepository.findByCustomerId(1L).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        BigDecimal itemTotal = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getMenuItem().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate taxes and delivery charges
        BigDecimal taxes = calculateTaxes(itemTotal);
        BigDecimal deliveryCharges = calculateDeliveryCharges(itemTotal);

        // Calculate total amount
        BigDecimal totalAmount = itemTotal.add(taxes).add(deliveryCharges);

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setItemTotal(itemTotal);
        order.setTaxes(taxes);
        order.setDeliveryCharges(deliveryCharges);
        order.setTotalAmount(totalAmount);
        order.setPaymentMode(paymentMode);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    private BigDecimal calculateDeliveryCharges(BigDecimal itemTotal) {
        BigDecimal taxRate = BigDecimal.valueOf(0.1); // Example: 10%
        return itemTotal.multiply(taxRate);
    }

    private BigDecimal calculateTaxes(BigDecimal itemTotal) {
        // Example: fixed rate of $5
        return BigDecimal.valueOf(5.00);
    }
}
