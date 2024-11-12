package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.UserRepository;
import com.mealmap.mealmap_backend_api.security.JWTService;
import com.mealmap.mealmap_backend_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;


    @Override
    public User signup(SignupDto signupDto, Set<Role> roles) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

        if (user != null) throw new RuntimeConflictException("Cannot signup, User already exists with email " +signupDto.getEmail());

        User mappedUser = modelMapper.map(signupDto, User.class);

        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        mappedUser.setRoles(roles);

        mappedUser.setActive(true);

        return userRepository.save(mappedUser);
    }

    @Override
    public String[] login(String email, String password) {

        User loginRequestedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+email));

        if(!loginRequestedUser.getActive()) {
            throw new RuntimeException("User account is deactivated.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User)authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
