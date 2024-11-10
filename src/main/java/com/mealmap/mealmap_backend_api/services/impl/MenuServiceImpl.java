package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuItemDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;
import com.mealmap.mealmap_backend_api.entities.Menu;
import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.MenuItemRepository;
import com.mealmap.mealmap_backend_api.respositories.MenuRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.MenuService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;
    private final RestaurantOwnerService restaurantOwnerService;

    @Override
    @Transactional
    public MenuDto createMenuForARestaurant(Long restaurantId, MenuRequestDto menuRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if (menuRepository.findByTitleAndRestaurant(menuRequestDto.getTitle(), restaurant).isPresent()) {
            throw new RuntimeConflictException("Menu with this title is already present in this restaurant");
        }

        Menu menu = new Menu();
        menu.setTitle(menuRequestDto.getTitle());
        menu.setRestaurant(restaurant);

        List<MenuItem> menuItems = menuRequestDto.getItems().stream().map(itemDTO -> {
            MenuItem item = new MenuItem();
            item.setName(itemDTO.getName());
            item.setDescription(itemDTO.getDescription());
            item.setPrice(itemDTO.getPrice());
            item.setAvailable(itemDTO.getAvailable());
            item.setMenu(menu);
            item.setActive(true);
            return item;
        }).toList();

        menu.setItems(menuItems);
        Menu savedMenu = menuRepository.save(menu);

        return modelMapper.map(savedMenu, MenuDto.class);
    }

    @Override
    public List<MenuDto> getMenuForARestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        return menuRepository.findByRestaurant(restaurant)
                .stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .toList();

    }

    @Override
    @Transactional
    public MenuDto addMenuItemInAMenu(Long restaurantId, Long menuId, MenuItemDto menuItem) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        List<MenuItem> items = menu.getItems();

        if (items.stream().anyMatch(m -> m.getName().equalsIgnoreCase(menuItem.getName())) ) {
           throw new RuntimeConflictException("Item already exists in the menu with name: "+menuItem.getName());
        }

        MenuItem newMenuItem = modelMapper.map(menuItem, MenuItem.class);

        newMenuItem.setActive(true);

        newMenuItem.setMenu(menu);

        MenuItem savedmenuItem = menuItemRepository.save(newMenuItem);

        items.add(savedmenuItem);

        menu.setItems(items);

        Menu savedMenu = menuRepository.save(menu);

        return modelMapper.map(savedMenu, MenuDto.class);
    }

    @Override
    public MenuItemDto getMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        MenuItem menuItem = menuItemRepository.findByMenuAndId(menu, menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu Item not found with id: " + menuItemId));

        return modelMapper.map(menuItem, MenuItemDto.class);
    }

    @Override
    public MenuItemDto updateAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId, MenuItemDto updatedMenuItemDto) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        MenuItem menuItem = menuItemRepository.findByMenuAndId(menu, menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItem.setName(updatedMenuItemDto.getName());
        menuItem.setDescription(updatedMenuItemDto.getDescription());
        menuItem.setPrice(updatedMenuItemDto.getPrice());
        menuItem.setAvailable(updatedMenuItemDto.getAvailable());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return modelMapper.map(savedMenuItem, MenuItemDto.class);
    }

    @Override
    public MenuItemDto updateAvailabilityOfAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        MenuItem menuItem = menuItemRepository.findByMenuAndId(menu, menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItem.setAvailable(!menuItem.getAvailable());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return modelMapper.map(savedMenuItem, MenuItemDto.class);
    }

    @Override
    public MenuItemDto updateActiveStateOfAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        MenuItem menuItem = menuItemRepository.findByMenuAndId(menu, menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItem.setActive(!menuItem.getActive());

        menuItem.setAvailable(!menuItem.getActive());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return modelMapper.map(savedMenuItem, MenuItemDto.class);
    }

    @Override
    public MenuDto getMenuForARestaurantById(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

        return modelMapper.map(menu, MenuDto.class);
    }
}
