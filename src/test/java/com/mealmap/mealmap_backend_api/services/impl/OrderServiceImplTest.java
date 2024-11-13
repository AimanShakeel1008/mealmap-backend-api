package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.OrderDto;
import com.mealmap.mealmap_backend_api.entities.*;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.respositories.*;
import com.mealmap.mealmap_backend_api.services.AdminService;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private DeliveryRequestRepository deliveryRequestRepository;

    @MockBean
    private CartService cartService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private RestaurantOwnerService restaurantOwnerService;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @MockBean
    private DeliveryPersonnelRepository deliveryPersonnelRepository;

    @MockBean
    private AdminService adminService;

    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl(cartRepository, orderRepository, deliveryRequestRepository, cartService, modelMapper, customerService, restaurantOwnerService, restaurantRepository, deliveryRepository, deliveryPersonnelRepository, adminService);
    }

    @Test
    void placeOrder_shouldReturnOrderDto_whenOrderIsSuccessfullyPlaced() {
        // Arrange
        PaymentMode paymentMode = PaymentMode.CASH_ON_DELIVERY;
        Customer customer = new Customer();
        Cart cart = new Cart();
        cart.setCustomer(customer);

        // Create Restaurant and Menu for MenuItem
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L); // Set any required fields of Restaurant

        Menu menu = new Menu();
        menu.setRestaurant(restaurant); // Set Restaurant for Menu

        // Create MenuItem and associate it with CartItem
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setMenu(menu); // Set Menu for MenuItem

        CartItem cartItem = new CartItem();
        cartItem.setSellingPrice(10.0); // Set selling price
        cartItem.setQuantity(2); // Set quantity
        cartItem.setMenuItem(menuItem); // Associate the CartItem with MenuItem

        cart.setItems(List.of(cartItem)); // Add CartItem to Cart

        when(customerService.getCurrentCustomer()).thenReturn(customer);
        when(cartRepository.findByCustomer(customer)).thenReturn(Optional.of(cart));

        // Create the order object that will be saved
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.PENDING); // Ensure the orderStatus is set correctly
        order.setItems(List.of(cartItem));
        order.setTotalAmount(BigDecimal.valueOf(30));

        // Mock repository save to return the created order
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Mock ModelMapper to return a new OrderDto
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);  // Set expected ID for the OrderDto
        orderDto.setOrderStatus(OrderStatus.PENDING);  // Ensure the status is also set here
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);

        // Act
        OrderDto result = orderService.placeOrder(paymentMode);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());  // Assert that the ID matches the expected value
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());  // Assert that the orderStatus is correctly mapped
        verify(orderRepository, times(1)).save(any(Order.class));
    }




    @Test
    void acceptOrder_shouldReturnOrderDto_whenOrderIsAccepted() {
        // Arrange
        Long orderId = 1L;
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        RestaurantOwner restaurantOwner = new RestaurantOwner();
        restaurantOwner.setId(1L);
        restaurant.setOwner(restaurantOwner);
        Order order = new Order();
        order.setId(orderId);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PENDING);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(restaurantOwnerService.getCurrentRestaurantOwner()).thenReturn(restaurantOwner);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);  // Set expected ID for the OrderDto
        orderDto.setOrderStatus(OrderStatus.CONFIRMED);  // Ensure the status is also set here
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);

        // Act
        OrderDto result = orderService.acceptOrder(orderId, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getOrderStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderByOrderId_shouldReturnOrderDto_whenOrderExists() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);  // Set expected ID for the OrderDto
        orderDto.setOrderStatus(OrderStatus.PENDING);  // Ensure the status is also set here
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);
        // Act
        OrderDto result = orderService.getOrderByOrderId(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getAllOrders_shouldReturnListOfOrderDtos_whenOrdersExist() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(new OrderDto());

        // Act
        List<OrderDto> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrdersOfACustomer_shouldReturnListOfOrderDtos_whenCustomerHasOrders() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);

        when(customerService.getCurrentCustomer()).thenReturn(customer);
        when(orderRepository.findByCustomer(customer)).thenReturn(orders);
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(new OrderDto());

        // Act
        List<OrderDto> result = orderService.getAllOrdersOfACustomer();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void cancelOrder_shouldReturnOrderDto_whenOrderIsCancelled() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);  // Set expected ID for the OrderDto
        orderDto.setOrderStatus(OrderStatus.CANCELLED);  // Ensure the status is also set here
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);
        // Act
        OrderDto result = orderService.cancelOrder(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELLED, result.getOrderStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateStatusToInProgress_shouldReturnOrderDto_whenOrderStatusIsUpdated() {
        // Arrange
        Long orderId = 1L;
        Long restaurantId = 1L;

        // Mock the restaurant repository to return a restaurant with the given ID
        RestaurantOwner restaurantOwner = RestaurantOwner.builder().id(1L).build();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwner(restaurantOwner); // Set the correct restaurant ID

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantOwnerService.getCurrentRestaurantOwner()).thenReturn(restaurantOwner);

        // Create a valid order associated with the restaurant
        Order order = new Order();
        order.setId(orderId);
        order.setRestaurant(restaurant);  // Ensure the order is associated with the correct restaurant
        order.setOrderStatus(OrderStatus.CONFIRMED);  // Initial status set to CONFIRMED

        // Mock the order repository to return the order when fetching by ID
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Mock save to return the updated order
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order updatedOrder = invocation.getArgument(0);
            updatedOrder.setOrderStatus(OrderStatus.IN_PROGRESS); // Ensure status is updated
            return updatedOrder;  // Return the updated order
        });

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);  // Set expected ID for the OrderDto
        orderDto.setOrderStatus(OrderStatus.IN_PROGRESS);  // Ensure the status is also set here
        when(modelMapper.map(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);

        // Act
        OrderDto result = orderService.updateStatusToInProgress(orderId, restaurantId);

        // Assert
        assertNotNull(result);  // Ensure that result is not null
        assertEquals(OrderStatus.IN_PROGRESS, result.getOrderStatus());  // Ensure the status was updated
        verify(orderRepository, times(1)).save(order);  // Ensure save was called once
    }





}
