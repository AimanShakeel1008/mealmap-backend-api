package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.RestaurantOwnerSignupDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.RestaurantOwnerRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantOwnerServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestaurantOwnerRepository restaurantOwnerRepository;

    @InjectMocks
    private RestaurantOwnerServiceImpl restaurantOwnerService;

    private User user;
    private RestaurantOwner restaurantOwner;
    private RestaurantOwnerSignupDto restaurantOwnerSignupDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "John Doe", "john.doe@example.com", "password", Set.of(Role.RESTAURANT_OWNER), true, LocalDateTime.now(), LocalDateTime.now());
        restaurantOwner = new RestaurantOwner(1L, user, "1234567890", null, LocalDateTime.now(), LocalDateTime.now());

        restaurantOwnerSignupDto = new RestaurantOwnerSignupDto("John Doe", "john.doe@example.com", "password", "1234567890");

        when(authService.signup(any(SignupDto.class), any(Set.class))).thenReturn(user);

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .active(true)
                .roles(Set.of(Role.RESTAURANT_OWNER))
                .build();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // Test Case 1: Test for register()
    @Test
    void testRegister_ShouldReturnUserDto() {
        UserDto result = restaurantOwnerService.register(restaurantOwnerSignupDto);

        assertNotNull(result, "The returned UserDto should not be null");
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("John Doe", result.getName());
        assertTrue(result.getActive());
    }

    // Test Case 2: Test for createNewRestaurantOwner()
    @Test
    void testCreateNewRestaurantOwner_ShouldCreateAndReturnRestaurantOwner() {
        when(restaurantOwnerRepository.save(any(RestaurantOwner.class))).thenReturn(restaurantOwner);

        RestaurantOwner createdRestaurantOwner = restaurantOwnerService.createNewRestaurantOwner(restaurantOwnerSignupDto, user);

        assertNotNull(createdRestaurantOwner);
        assertEquals("1234567890", createdRestaurantOwner.getContactNumber());
        assertEquals(user, createdRestaurantOwner.getUser());
        verify(restaurantOwnerRepository, times(1)).save(any(RestaurantOwner.class));
    }

    // Test Case 3: Test for getCurrentRestaurantOwner()
    @Test
    void testGetCurrentRestaurantOwner_ShouldReturnRestaurantOwner() {
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(restaurantOwnerRepository.findByUser(user)).thenReturn(Optional.of(restaurantOwner));

        RestaurantOwner result = restaurantOwnerService.getCurrentRestaurantOwner();

        assertNotNull(result);
        assertEquals("1234567890", result.getContactNumber());
        verify(restaurantOwnerRepository, times(1)).findByUser(user);
    }

    // Test Case 4: Test for getCurrentRestaurantOwner() when restaurant owner is not found
    @Test
    void testGetCurrentRestaurantOwner_ShouldThrowResourceNotFoundException() {
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(restaurantOwnerRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantOwnerService.getCurrentRestaurantOwner());
    }
}
