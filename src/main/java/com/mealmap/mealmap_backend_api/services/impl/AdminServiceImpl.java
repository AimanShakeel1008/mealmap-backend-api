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
import com.mealmap.mealmap_backend_api.services.AdminService;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private  final ModelMapper modelMapper;
    private final AuthService authService;

    @Override
    public UserDto deactivateUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        user.setActive(false);

        User saveduser = userRepository.save(user);

        return modelMapper.map(saveduser, UserDto.class);
    }

    @Override
    public UserDto activateUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        user.setActive(true);

        User saveduser = userRepository.save(user);

        return modelMapper.map(saveduser, UserDto.class);
    }

    @Override
    public Admin getCurrentAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return adminRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Admin not associated with user with id: "+user.getId()
        ));
    }

    @Override
    @Transactional
    public UserDto register(AdminSignupDto adminSignUpDto) {
        SignupDto signupDto = SignupMapper.extractUserSpecificDetails(adminSignUpDto);

        User savedUser = authService.signup(signupDto, Set.of(Role.ADMIN));

        createNewAdmin(adminSignUpDto, savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    private Admin createNewAdmin(AdminSignupDto adminSignUpDto, User savedUser) {

        Admin admin = Admin.builder()
                .user(savedUser)
                .contactNumber(adminSignUpDto.getContactNumber())
                .build();

        return adminRepository.save(admin);
    }
}
