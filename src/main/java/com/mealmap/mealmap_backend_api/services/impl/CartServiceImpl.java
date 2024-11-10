package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.CartDto;
import com.mealmap.mealmap_backend_api.entities.Cart;
import com.mealmap.mealmap_backend_api.entities.CartItem;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CartItemRepository;
import com.mealmap.mealmap_backend_api.respositories.CartRepository;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.respositories.MenuItemRepository;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDto getCartOfACustomer() {

        Customer currentCustomer = customerService.getCurrentCustomer();

        Customer customer = customerRepository.findById(currentCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer with id: " + currentCustomer.getId()));

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto addToCart(Long cartId, Long menuItemId, int quantity) {

        Customer currentCustomer = customerService.getCurrentCustomer();

        Customer customer = customerRepository.findById(currentCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        if(!Objects.equals(cartId, cart.getId())) {
            throw new RuntimeException("Cannot add in cart as you are not the owner of this cart.");
        }

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getAvailable() || !menuItem.getActive()) {
            throw new RuntimeException("Item currently unavailable.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(quantity);
        cartItem.setSellingPrice(menuItem.getPrice());
        cartItem.setCart(cart);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        cart.getItems().add(savedCartItem);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public CartDto updateQuantityOfACartItem(Long cartId, Long cartItemId, int newQuantity) {
        {
            Customer currentCustomer = customerService.getCurrentCustomer();

            Customer customer = customerRepository.findById(currentCustomer.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

            Cart cart = cartRepository.findByCustomer(customer)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

            if (!Objects.equals(cartId, cart.getId())) {
                throw new RuntimeException("Cannot update cart as you are not the owner of this cart.");
            }

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            } else {
                cart.getItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            }

            Cart savedCart = cartRepository.save(cart);

            return modelMapper.map(savedCart, CartDto.class);
        }
    }

    @Override
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
