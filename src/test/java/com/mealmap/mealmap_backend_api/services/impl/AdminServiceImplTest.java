package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.AdminSignupDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Admin;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.AdminRepository;
import com.mealmap.mealmap_backend_api.respositories.UserRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeactivateUser_Success() {
        User user = new User();
        user.setEmail("admin@example.com");
        user.setActive(true);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto result = adminService.deactivateUser("admin@example.com");

        assertFalse(user.getActive());
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void testDeactivateUser_UserNotFound() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.deactivateUser("admin@example.com"));
    }

    @Test
    void testActivateUser_Success() {
        User user = new User();
        user.setEmail("admin@example.com");
        user.setActive(false);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto result = adminService.activateUser("admin@example.com");

        assertTrue(user.getActive());
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void testActivateUser_UserNotFound() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.activateUser("admin@example.com"));
    }

    @Test
    void testGetCurrentAdmin_Success() {
        User user = new User();
        user.setId(1L);
        Admin admin = new Admin();
        admin.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        when(adminRepository.findByUser(user)).thenReturn(Optional.of(admin));

        Admin result = adminService.getCurrentAdmin();

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void testGetCurrentAdmin_AdminNotFound() {
        User user = new User();
        user.setId(1L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        when(adminRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getCurrentAdmin());
    }

    @Test
    void testRegister_Success() {
        // Create mock data
        AdminSignupDto adminSignupDto = new AdminSignupDto();
        SignupDto signupDto = new SignupDto();
        User savedUser = new User();
        savedUser.setId(1L);  // Mock user with an ID
        Admin admin = new Admin();
        admin.setUser(savedUser);  // Associate the user with the admin

        // Mock static method SignupMapper.extractUserSpecificDetails
        try (MockedStatic<SignupMapper> mockedSignupMapper = mockStatic(SignupMapper.class)) {
            mockedSignupMapper.when(() -> SignupMapper.extractUserSpecificDetails(adminSignupDto))
                    .thenReturn(signupDto);  // Mock SignupMapper to return the signupDto

            // Mock other dependencies
            when(authService.signup(signupDto, Set.of(Role.ADMIN))).thenReturn(savedUser);
            when(adminRepository.findByUser(savedUser)).thenReturn(Optional.of(admin));
            when(modelMapper.map(savedUser, UserDto.class)).thenReturn(new UserDto());

            // Call the method to test
            UserDto result = adminService.register(adminSignupDto);

            // Assertions to verify the behavior
            assertNotNull(result);  // Ensure result is not null
            assertEquals(new UserDto(), result);  // Ensure result matches the mocked userDto
        }
    }

}

