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
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeliveryPersonnelServiceImpl implements DeliveryPersonnelService {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final DeliveryPersonnelRepository deliveryPersonnelRepository;

    @Override
    @Transactional
    public UserDto register(DeliveryPersonnelSignupDto deliveryPersonnelSignupDto) {
        SignupDto signupDto = SignupMapper.extractUserSpecificDetails(deliveryPersonnelSignupDto);
        User savedUser = authService.signup(signupDto, Set.of(Role.DELIVERY_PERSONNEL));
        createNewDeliveryPersonnel(deliveryPersonnelSignupDto, savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public DeliveryPersonnel getCurrentDeliveryPersonnel() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return deliveryPersonnelRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Customer not associated with user with id: "+user.getId()
        ));
    }

    @Override
    public DeliveryPersonnel createNewDeliveryPersonnel(DeliveryPersonnelSignupDto deliveryPersonnelSignupDto, User user) {

        DeliveryPersonnel deliveryPersonnel = DeliveryPersonnel.builder()
                .user(user)
                .contactNumber(deliveryPersonnelSignupDto.getContactNumber())
                .available(true)
                .build();

        return deliveryPersonnelRepository.save(deliveryPersonnel);
    }

    @Override
    public DeliveryPersonnel updateDeliveryPersonnelAvailability(DeliveryPersonnel deliveryPersonnel, boolean available) {

        deliveryPersonnel.setAvailable(available);

        return deliveryPersonnelRepository.save(deliveryPersonnel);
    }
}
