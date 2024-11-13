package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.Cart;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CartRepository;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.*;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final CustomerRepository customerRepository;
    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;
    private final CartRepository cartRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public UserDto register(CustomerSignupDto customerSignupDto) {

        SignupDto signupDto = SignupMapper.extractUserSpecificDetails(customerSignupDto);

        User savedUser = authService.signup(signupDto, Set.of(Role.CUSTOMER));

        createNewCustomer(customerSignupDto, savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    @Transactional
    public Customer createNewCustomer(CustomerSignupDto customerSignupDto, User user) {

        Customer customer = Customer.builder()
                .user(user)
                .address(customerSignupDto.getAddress())
                .contactNumber(customerSignupDto.getContactNumber())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Cart cart = cartRepository.findByCustomer(customer).orElseGet(Cart::new);

        cart.setCustomer(savedCustomer);

        cartRepository.save(cart);

        return savedCustomer;
    }

    @Override
    public Customer getCurrentCustomer() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Customer not associated with user with id: "+user.getId()
        ));
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants()
                .stream()
                .filter(RestaurantDto::getActive)
                .toList();
    }

    @Override
    public RestaurantDto getRestaurantById(Long restaurantId) {
        RestaurantDto restaurantDto = restaurantService.getRestaurantById(restaurantId);

        if(!restaurantDto.getActive()) {
            throw new ResourceNotFoundException("No active restaurant found with id: "+restaurantId);
        }

        return  restaurantDto;
    }

    @Override
    public RestaurantDto getRestaurantByName(String restaurantName) {

        RestaurantDto restaurantDto = restaurantService.getRestaurantByName(restaurantName);

        if(!restaurantDto.getActive()) {
            throw new ResourceNotFoundException("No active restaurant found with name: "+restaurantName);
        }

        return  restaurantDto;
    }

    @Override
    public List<RestaurantDto> getRestaurantsByDishName(String dishName) {
        return menuItemService.getRestaurantsByDishName(dishName)
                .stream()
                .filter(RestaurantDto::getActive)
                .toList();
    }

    @Override
    public List<RestaurantDto> getRestaurantsByCuisine(String cuisineType) {
        return restaurantService.getRestaurantsByCuisine(cuisineType)
                .stream()
                .filter(RestaurantDto::getActive)
                .toList();
    }

    @Override
    public List<MenuDto> getMenuForARestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if(!restaurant.getActive()) throw new ResourceNotFoundException("No active restaurant found with id:"+restaurantId);

        List<MenuDto> menuForARestaurant = menuService.getMenuForARestaurant(restaurantId);


        menuForARestaurant.forEach(menu ->
                menu.setItems(
                        menu.getItems()
                                .stream()
                                .filter(MenuItemDto::getActive)
                                .collect(Collectors.toList())
                )
        );

        return menuForARestaurant;
    }

    @Override
    public MenuDto getMenuForARestaurantById(Long restaurantId, Long menuId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if(!restaurant.getActive()) throw new ResourceNotFoundException("No active restaurant found with id:"+restaurantId);

        MenuDto menu = menuService.getMenuForARestaurantById(restaurantId, menuId);

        menu.setItems(
                menu.getItems()
                        .stream()
                        .filter(MenuItemDto::getActive)
                        .collect(Collectors.toList())
        );

        return menu;
    }

    @Override
    public MenuItemDto getMenuItemForARestaurantById(Long restaurantId, Long menuId, Long menuItemId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if(!restaurant.getActive()) throw new ResourceNotFoundException("No active restaurant found with id:"+restaurantId);

        MenuItemDto menuItem = menuService.getMenuItemInAMenu(restaurantId, menuId, menuItemId);

        if(!menuItem.getActive()) {
            throw new ResourceNotFoundException("No active menu item found with id: "+menuItemId);
        }
        return menuItem;
    }
}
