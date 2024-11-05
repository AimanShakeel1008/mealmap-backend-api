package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.CartDto;
import com.mealmap.mealmap_backend_api.entities.Cart;
import com.mealmap.mealmap_backend_api.entities.CartItem;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CartRepository;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.respositories.MenuItemRepository;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Override
    public CartDto addToCart(Long menuItemId, int quantity) {

        Customer currentCustomer = customerService.getCurrentCustomer();

        Customer customer = customerRepository.findById(currentCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseGet(Cart::new);

        cart.setCustomer(customer);

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getAvailable()) {
            throw new RuntimeException("Item currently unavailable.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);

        cart.getItems().add(cartItem);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
