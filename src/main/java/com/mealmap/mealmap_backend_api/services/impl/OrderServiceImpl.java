package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.OrderDto;
import com.mealmap.mealmap_backend_api.entities.*;
import com.mealmap.mealmap_backend_api.entities.enums.*;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.*;
import com.mealmap.mealmap_backend_api.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRequestRepository deliveryRequestRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final RestaurantOwnerService restaurantOwnerService;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryPersonnelRepository deliveryPersonnelRepository;
    private final AdminService adminService;

    @Override
    public OrderDto placeOrder(PaymentMode paymentMode) {
        Customer currentCustomer = customerService.getCurrentCustomer();
        Cart cart = cartRepository.findByCustomer(currentCustomer).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        BigDecimal itemTotal = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getSellingPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate taxes and delivery charges
        BigDecimal taxes = calculateTaxes(itemTotal);
        BigDecimal deliveryCharges = calculateDeliveryCharges(itemTotal);

        // Calculate total amount
        BigDecimal totalAmount = itemTotal.add(taxes).add(deliveryCharges);

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setRestaurant(cart.getItems().getFirst().getMenuItem().getMenu().getRestaurant());
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

    @Override
    @Transactional
    public OrderDto acceptOrder(Long orderId, Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!Objects.equals(order.getRestaurant().getId(), restaurantId)) {
            throw new RuntimeException("Order cannot be accepted as this order is not for this restaurant.");
        }

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not accept the order as you are not the owner of this restaurant");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);

        Order savedOrder = orderRepository.save(order);

        DeliveryRequest deliveryRequest = new DeliveryRequest();

        deliveryRequest.setOrder(savedOrder);
        deliveryRequest.setDeliveryRequestStatus(DeliveryRequestStatus.PENDING);

        deliveryRequestRepository.save(deliveryRequest);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateStatusToInProgress(Long orderId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!Objects.equals(order.getRestaurant().getId(), restaurantId)) {
            throw new RuntimeException("Order status cannot be updated as this order is not for this restaurant.");
        }

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the order status as you are not the owner of this restaurant");
        }

        if(!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new RuntimeException("Order status cannot be updated as this order has not yest been confirmed");
        }

        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateStatusToOrderReady(Long orderId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!Objects.equals(order.getRestaurant().getId(), restaurantId)) {
            throw new RuntimeException("Order status cannot be updated as this order is not for this restaurant.");
        }

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the order status as you are not the owner of this restaurant.");
        }

        if(!order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new RuntimeException("Order status cannot be updated as this order has not been in progress.");
        }

        order.setOrderStatus(OrderStatus.READY);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersOfARestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not get the list of orders as you are not the owner of this restaurant.");
        }

        List<Order> orderList = orderRepository.findByRestaurant(restaurant);

        return orderList.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    @Override
    public OrderDto getOrderOfARestaurantByOrderId(Long restaurantId, Long orderId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not get the details of orders as you are not the owner of this restaurant.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+orderId));

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersOfACustomer() {
        Customer currentCustomer = customerService.getCurrentCustomer();

        List<Order> orderList = orderRepository.findByCustomer(currentCustomer);

        return orderList.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    @Override
    public OrderDto getOrdersOfACustomerByOrderId(Long orderId) {
        Customer currentCustomer = customerService.getCurrentCustomer();

        Order order = orderRepository.findByCustomerAndId(currentCustomer, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+orderId));

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrders() {

        List<Order> allOrders = orderRepository.findAll();

        return allOrders.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    @Override
    public OrderDto getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not found with id: " + orderId));

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not found with id: " + orderId));

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        Optional<DeliveryRequest> optionalDeliveryRequest = deliveryRequestRepository.findByOrder(order);

        if(optionalDeliveryRequest.isPresent()) {
            DeliveryRequest deliveryRequest = optionalDeliveryRequest.get();

            deliveryRequest.setDeliveryRequestStatus(DeliveryRequestStatus.CANCELLED);

            deliveryRequestRepository.save(deliveryRequest);
        }

        Optional<Delivery> optionalDelivery = deliveryRepository.findByOrder(order);

        if(optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();

            delivery.setDeliveryStatus(DeliveryStatus.CANCELLED);

            deliveryRepository.save(delivery);

            DeliveryPersonnel deliveryPersonnel = delivery.getDeliveryPersonnel();

            deliveryPersonnel.setAvailable(true);

            deliveryPersonnelRepository.save(deliveryPersonnel);
        }

        return modelMapper.map(savedOrder, OrderDto.class);

    }

    @Override
    public OrderDto updateStatusToReceivedByDeliveryPersonnel(Long orderId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!Objects.equals(order.getRestaurant().getId(), restaurantId)) {
            throw new RuntimeException("Order status cannot be updated as this order is not for this restaurant.");
        }

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the order status as you are not the owner of this restaurant.");
        }

        if(!order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new RuntimeException("Order status cannot be updated as this order is not ready yet.");
        }

        DeliveryRequest deliveryRequest = deliveryRequestRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find delivery request associated with order id: " + orderId));

        Delivery delivery = deliveryRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find delivery associated with order id: " + orderId));

        if(!deliveryRequest.getDeliveryRequestStatus().equals(DeliveryRequestStatus.CONFIRMED) ||
                !delivery.getDeliveryStatus().equals(DeliveryStatus.ORDER_PICKED_UP)) {
            throw new RuntimeException("Order status cannot be updated as delivery personnel not yet picked up the order.");
        }


        order.setOrderStatus(OrderStatus.RECEIVED_BY_DELIVERY_PERSONNEL);

        Order savedOrder = orderRepository.save(order);

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
