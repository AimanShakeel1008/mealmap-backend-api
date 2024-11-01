package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final CustomerRepository customerRepository;

    @Override
    @Transactional
    public UserDto register(SignupDto signupDto) {
        User savedUser = authService.signup(signupDto, Set.of(Role.CUSTOMER));
        createNewCustomer(savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public Customer createNewCustomer(User user) {

        Customer customer = Customer.builder()
                .user(user)
                .build();

        return customerRepository.save(customer);
    }
}
