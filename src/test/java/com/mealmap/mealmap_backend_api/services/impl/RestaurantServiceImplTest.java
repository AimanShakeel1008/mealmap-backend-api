package com.mealmap.mealmap_backend_api.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @Mock private RestaurantRepository restaurantRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private RestaurantOwnerService restaurantOwnerService;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Manually initialize mocks
        restaurantService = new RestaurantServiceImpl(restaurantRepository, modelMapper, restaurantOwnerService);
    }

    @Test
    void testCreateNewRestaurant_Success() {
        // Create mock data
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Test Restaurant");
        restaurantDto.setAddress("Test Address");

        RestaurantOwner currentRestaurantOwner = new RestaurantOwner();
        currentRestaurantOwner.setId(1L);

        Restaurant existingRestaurant = null;  // No existing restaurant with the same name

        // Mocks
        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(Optional.ofNullable(existingRestaurant));
        when(restaurantOwnerService.getCurrentRestaurantOwner()).thenReturn(currentRestaurantOwner);
        when(modelMapper.map(restaurantDto, Restaurant.class)).thenReturn(new Restaurant());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(new Restaurant());
        when(modelMapper.map(any(Restaurant.class), eq(RestaurantDto.class))).thenReturn(restaurantDto);  // Correct stubbing for Restaurant -> RestaurantDto mapping

        // Call the method
        RestaurantDto result = restaurantService.createNewRestaurant(restaurantDto);

        // Assertions
        assertNotNull(result);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));  // Ensure save was called once
    }

    @Test
    void testCreateNewRestaurant_Conflict() {
        // Create mock data
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Test Restaurant");

        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setName("Test Restaurant");

        // Mocks
        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(Optional.of(existingRestaurant));

        // Call the method and expect exception
        RuntimeConflictException exception = assertThrows(RuntimeConflictException.class, () -> {
            restaurantService.createNewRestaurant(restaurantDto);
        });

        assertEquals("Cannot register restaurant, restaurant already exists with name Test Restaurant", exception.getMessage());
    }

    @Test
    void testGetRestaurantById_Success() {
        // Create mock data
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        RestaurantDto restaurantDto = new RestaurantDto();

        // Mocks
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(modelMapper.map(restaurant, RestaurantDto.class)).thenReturn(restaurantDto);

        // Call the method
        RestaurantDto result = restaurantService.getRestaurantById(restaurantId);

        // Assertions
        assertNotNull(result);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        // Create mock data
        Long restaurantId = 1L;

        // Mocks
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Call the method and expect exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restaurantService.getRestaurantById(restaurantId);
        });

        assertEquals("Restaurant not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetRestaurantByName_Success() {
        // Create mock data
        String restaurantName = "Test Restaurant";
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        RestaurantDto restaurantDto = new RestaurantDto();

        // Mocks
        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.of(restaurant));
        when(modelMapper.map(restaurant, RestaurantDto.class)).thenReturn(restaurantDto);

        // Call the method
        RestaurantDto result = restaurantService.getRestaurantByName(restaurantName);

        // Assertions
        assertNotNull(result);
    }

    @Test
    void testGetRestaurantByName_NotFound() {
        // Create mock data
        String restaurantName = "Test Restaurant";

        // Mocks
        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.empty());

        // Call the method and expect exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restaurantService.getRestaurantByName(restaurantName);
        });

        assertEquals("Restaurant not found with name: Test Restaurant", exception.getMessage());
    }

    @Test
    void testUpdateRestaurantDetails_Success() {
        // Create mock data
        Long restaurantId = 1L;
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Updated Name");
        restaurantDto.setAddress("Updated Address");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwner(RestaurantOwner.builder().id(1L).build()); // Mock the owner of the restaurant
        RestaurantOwner currentRestaurantOwner = new RestaurantOwner();
        currentRestaurantOwner.setId(1L); // Mock current owner

        // Mocks
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantOwnerService.getCurrentRestaurantOwner()).thenReturn(currentRestaurantOwner);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(modelMapper.map(any(Restaurant.class), eq(RestaurantDto.class))).thenReturn(restaurantDto);  // Correct stubbing for Restaurant -> RestaurantDto mapping

        // Call the method
        RestaurantDto result = restaurantService.updateRestaurantDetails(restaurantId, restaurantDto);

        // Assertions
        assertNotNull(result);
        assertEquals(restaurantDto.getName(), result.getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));  // Ensure save was called once
    }

    @Test
    void testUpdateRestaurantDetails_NotOwner() {
        // Create mock data
        Long restaurantId = 1L;
        RestaurantDto restaurantDto = new RestaurantDto();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwner(new RestaurantOwner()); // Set mock owner
        RestaurantOwner currentRestaurantOwner = new RestaurantOwner();
        currentRestaurantOwner.setId(2L); // Different owner ID to trigger error

        // Mocks
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantOwnerService.getCurrentRestaurantOwner()).thenReturn(currentRestaurantOwner);

        // Call the method and expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.updateRestaurantDetails(restaurantId, restaurantDto);
        });

        assertEquals("You can not update the details of the restaurant as you are not the owner of this restaurant", exception.getMessage());
    }
}

