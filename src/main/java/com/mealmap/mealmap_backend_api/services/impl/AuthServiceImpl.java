package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.UserRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User signup(SignupDto signupDto, Set<Role> roles) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

        if (user != null) throw new RuntimeConflictException("Cannot signup, User already exists with email " +signupDto.getEmail());

        User mappedUser = modelMapper.map(signupDto, User.class);

        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        mappedUser.setRoles(roles);

        return userRepository.save(mappedUser);
    }
}
