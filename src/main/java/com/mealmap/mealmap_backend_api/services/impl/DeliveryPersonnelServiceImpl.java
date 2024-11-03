package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.respositories.DeliveryPersonnelRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public UserDto register(SignupDto signupDto) {
        User savedUser = authService.signup(signupDto, Set.of(Role.DELIVERY_PERSONNEL));
        createNewDeliveryPersonnel(savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public DeliveryPersonnel createNewDeliveryPersonnel(User user) {

        DeliveryPersonnel deliveryPersonnel = DeliveryPersonnel.builder()
                .user(user)
                .available(true)
                .build();

        return deliveryPersonnelRepository.save(deliveryPersonnel);
    }
}
