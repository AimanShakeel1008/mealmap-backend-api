package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.Cart;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CartRepository;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.MenuItemService;
import com.mealmap.mealmap_backend_api.services.MenuService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private MenuService menuService;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private User user;
    private Customer customer;
    private CustomerSignupDto customerSignupDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mocks for user and customer
        user = new User(1L, "John Doe","john.doe@example.com", "password", Set.of(Role.CUSTOMER), true, LocalDateTime.now(), LocalDateTime.now());
        customer = new Customer(1L, user, "123 Street", "1234567890", LocalDateTime.now(), LocalDateTime.now());

        customerSignupDto = new CustomerSignupDto("John Doe", "john.doe@example.com", "password", "123 Street", "1234567890");

        when(authService.signup(any(SignupDto.class), any(Set.class))).thenReturn(user);

        // Mock ModelMapper to convert User to UserDto
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .active(true)
                .roles(Set.of(Role.CUSTOMER))
                .build();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Mock the SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);    }

    // Test Case 1: Test for register()
    @Test
    void testRegister_ShouldReturnUserDto() {
        // Call the register method
        UserDto result = customerService.register(customerSignupDto);

        // Assert the result
        assertNotNull(result, "The returned UserDto should not be null");
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("John Doe", result.getName());
        assertTrue(result.getActive());
    }

    // Test Case 2: Test for createNewCustomer()
    @Test
    void testCreateNewCustomer_ShouldCreateAndReturnCustomer() {
        // Mock behavior
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(cartRepository.findByCustomer(any())).thenReturn(Optional.empty());

        // Call method
        Customer createdCustomer = customerService.createNewCustomer(customerSignupDto, user);

        // Verifications
        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.getUser().getName());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    // Test Case 3: Test for getCurrentCustomer()
    @Test
    void testGetCurrentCustomer_ShouldReturnCustomer() {
        // Mock behavior
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(customerRepository.findByUser(user)).thenReturn(Optional.of(customer));

        // Call method
        Customer result = customerService.getCurrentCustomer();

        // Verifications
        assertNotNull(result);
        assertEquals("123 Street", result.getAddress());
        verify(customerRepository, times(1)).findByUser(user);
    }

    // Test Case 4: Test for getCurrentCustomer() when customer is not found
    @Test
    void testGetCurrentCustomer_ShouldThrowResourceNotFoundException() {
        // Mock behavior
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(customerRepository.findByUser(user)).thenReturn(Optional.empty());

        // Call method and verify exception
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCurrentCustomer());
    }

    // Test Case 5: Test for getRestaurantById() when the restaurant is active
    @Test
    void testGetRestaurantById_ShouldReturnRestaurantDto() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", true, true);
        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurantDto);

        // Call method
        RestaurantDto result = customerService.getRestaurantById(1L);

        // Verifications
        assertNotNull(result);
        assertEquals("Restaurant", result.getName());
    }

    // Test Case 6: Test for getRestaurantById() when the restaurant is inactive
    @Test
    void testGetRestaurantById_ShouldThrowResourceNotFoundException_WhenRestaurantIsInactive() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", false, false);
        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurantDto);

        // Call method and verify exception
        assertThrows(ResourceNotFoundException.class, () -> customerService.getRestaurantById(1L));
    }

    // Test Case 7: Test for getRestaurantByName()
    @Test
    void testGetRestaurantByName_ShouldReturnRestaurantDto() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", true, true);
        when(restaurantService.getRestaurantByName("Restaurant")).thenReturn(restaurantDto);

        // Call method
        List<RestaurantDto> result = customerService.searchRestaurantByName("Restaurant");

        // Verifications
        assertNotNull(result);
        assertEquals("Restaurant", result.get(0).getName());
    }

    // Test Case 8: Test for getRestaurantByName() when the restaurant is inactive
    @Test
    void testGetRestaurantByName_ShouldThrowResourceNotFoundException_WhenRestaurantIsInactive() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", false, false);
        when(restaurantService.getRestaurantByName("Restaurant")).thenReturn(restaurantDto);

        // Call method and verify exception
        assertThrows(ResourceNotFoundException.class, () -> customerService.searchRestaurantByName("Restaurant"));
    }

    // Test Case 9: Test for getRestaurantsByDishName()
    @Test
    void testGetRestaurantsByDishName_ShouldReturnListOfActiveRestaurants() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", true, true);
        when(menuItemService.getRestaurantsByDishName("Pizza")).thenReturn(List.of(restaurantDto));

        // Call method
        List<RestaurantDto> result = customerService.getRestaurantsByDishName("Pizza");

        // Verifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Restaurant", result.get(0).getName());
    }

    // Test Case 10: Test for getRestaurantsByCuisine()
    @Test
    void testGetRestaurantsByCuisine_ShouldReturnListOfActiveRestaurants() {
        // Mock behavior
        RestaurantDto restaurantDto = new RestaurantDto(1L, "Restaurant", "123 Street", "9 AM - 9 PM", "Italian", "1234567890", true, true);
        when(restaurantService.getRestaurantsByCuisine("Italian")).thenReturn(List.of(restaurantDto));

        // Call method
        List<RestaurantDto> result = customerService.getRestaurantsByCuisine("Italian");

        // Verifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Restaurant", result.get(0).getName());
    }

    // Test Case 11: Test for getMenuForARestaurant()
    @Test
    void testGetMenuForARestaurant_ShouldReturnFilteredMenu() {
        // Mock behavior
        Restaurant restaurant = new Restaurant();
        restaurant.setActive(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        MenuDto menuDto = new MenuDto(1L, "Menu", List.of(new MenuItemDto(1L, "Pizza", "Delicious", 10.0, true, true)));
        when(menuService.getMenuForARestaurant(1L)).thenReturn(List.of(menuDto));

        // Call method
        List<MenuDto> result = customerService.getMenuForARestaurant(1L);

        // Verifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Menu", result.get(0).getTitle());
    }

    // Test Case 12: Test for getMenuItemForARestaurantById()
    @Test
    void testGetMenuItemForARestaurantById_ShouldReturnActiveMenuItem() {
        // Mock behavior
        Restaurant restaurant = new Restaurant();
        restaurant.setActive(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        MenuItemDto menuItemDto = new MenuItemDto(1L, "Pizza", "Delicious", 10.0, true, true);
        when(menuService.getMenuItemInAMenu(1L, 1L, 1L)).thenReturn(menuItemDto);

        // Call method
        MenuItemDto result = customerService.getMenuItemForARestaurantById(1L, 1L, 1L);

        // Verifications
        assertNotNull(result);
        assertEquals("Pizza", result.getName());
    }

    // Test Case 13: Test for getMenuItemForARestaurantById() when menu item is inactive
    @Test
    void testGetMenuItemForARestaurantById_ShouldThrowResourceNotFoundException_WhenMenuItemIsInactive() {
        // Mock behavior
        Restaurant restaurant = new Restaurant();
        restaurant.setActive(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        MenuItemDto menuItemDto = new MenuItemDto(1L, "Pizza", "Delicious", 10.0, false, false);
        when(menuService.getMenuItemInAMenu(1L, 1L, 1L)).thenReturn(menuItemDto);

        // Call method and verify exception
        assertThrows(ResourceNotFoundException.class, () -> customerService.getMenuItemForARestaurantById(1L, 1L, 1L));
    }
}
