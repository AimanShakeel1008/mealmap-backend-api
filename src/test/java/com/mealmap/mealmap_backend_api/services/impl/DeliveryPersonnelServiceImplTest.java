package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.DeliveryPersonnelSignupDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.DeliveryPersonnelRepository;
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

class DeliveryPersonnelServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DeliveryPersonnelRepository deliveryPersonnelRepository;

    @InjectMocks
    private DeliveryPersonnelServiceImpl deliveryPersonnelService;

    private User user;
    private DeliveryPersonnel deliveryPersonnel;
    private DeliveryPersonnelSignupDto deliveryPersonnelSignupDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize user and delivery personnel mocks
        user = new User(1L, "Jane Doe", "jane.doe@example.com", "password", Set.of(Role.DELIVERY_PERSONNEL), true, LocalDateTime.now(), LocalDateTime.now());
        deliveryPersonnel = new DeliveryPersonnel(1L, user, true, "1234567890", LocalDateTime.now(), LocalDateTime.now());

        deliveryPersonnelSignupDto = new DeliveryPersonnelSignupDto("Jane Doe", "jane.doe@example.com", "password", "1234567890");

        when(authService.signup(any(SignupDto.class), any(Set.class))).thenReturn(user);

        // Mock ModelMapper to convert User to UserDto
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .active(true)
                .roles(Set.of(Role.DELIVERY_PERSONNEL))
                .build();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Mock SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // Test Case 1: Test for register()
    @Test
    void testRegister_ShouldReturnUserDto() {
        // Call the register method
        UserDto result = deliveryPersonnelService.register(deliveryPersonnelSignupDto);

        // Verify and assert results
        assertNotNull(result, "The returned UserDto should not be null");
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("Jane Doe", result.getName());
        assertTrue(result.getActive());
    }

    // Test Case 2: Test for createNewDeliveryPersonnel()
    @Test
    void testCreateNewDeliveryPersonnel_ShouldCreateAndReturnDeliveryPersonnel() {
        // Mock behavior
        when(deliveryPersonnelRepository.save(any(DeliveryPersonnel.class))).thenReturn(deliveryPersonnel);

        // Call method
        DeliveryPersonnel createdPersonnel = deliveryPersonnelService.createNewDeliveryPersonnel(deliveryPersonnelSignupDto, user);

        // Verifications
        assertNotNull(createdPersonnel);
        assertEquals("Jane Doe", createdPersonnel.getUser().getName());
        assertTrue(createdPersonnel.getAvailable());
        assertEquals("1234567890", createdPersonnel.getContactNumber());
    }

    // Test Case 3: Test for getCurrentDeliveryPersonnel()
    @Test
    void testGetCurrentDeliveryPersonnel_ShouldReturnDeliveryPersonnel() {
        // Mock behavior
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(deliveryPersonnelRepository.findByUser(user)).thenReturn(Optional.of(deliveryPersonnel));

        // Call method
        DeliveryPersonnel result = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        // Verifications
        assertNotNull(result);
        assertEquals("1234567890", result.getContactNumber());
        verify(deliveryPersonnelRepository, times(1)).findByUser(user);
    }

    // Test Case 4: Test for getCurrentDeliveryPersonnel() when delivery personnel is not found
    @Test
    void testGetCurrentDeliveryPersonnel_ShouldThrowResourceNotFoundException() {
        // Mock behavior
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(deliveryPersonnelRepository.findByUser(user)).thenReturn(Optional.empty());

        // Call method and verify exception
        assertThrows(ResourceNotFoundException.class, () -> deliveryPersonnelService.getCurrentDeliveryPersonnel());
    }

    // Test Case 5: Test for updateDeliveryPersonnelAvailability()
    @Test
    void testUpdateDeliveryPersonnelAvailability_ShouldUpdateAvailabilityStatus() {
        // Mock behavior
        when(deliveryPersonnelRepository.save(any(DeliveryPersonnel.class))).thenReturn(deliveryPersonnel);

        // Call method to set availability to false
        DeliveryPersonnel updatedPersonnel = deliveryPersonnelService.updateDeliveryPersonnelAvailability(deliveryPersonnel, false);

        // Verifications
        assertNotNull(updatedPersonnel);
        assertFalse(updatedPersonnel.getAvailable());
        verify(deliveryPersonnelRepository, times(1)).save(deliveryPersonnel);
    }
}

