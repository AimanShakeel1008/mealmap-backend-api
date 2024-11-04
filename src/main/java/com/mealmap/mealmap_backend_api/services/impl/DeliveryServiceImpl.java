package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.entities.Delivery;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.Order;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.DeliveryRepository;
import com.mealmap.mealmap_backend_api.respositories.OrderRepository;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.services.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPersonnelService deliveryPersonnelService;
    private final ModelMapper modelMapper;

    @Override
    public List<DeliveryDto> getAllDeliveries() {
        List<Delivery>  deliveries = deliveryRepository.findByDeliveryPersonnel(DeliveryPersonnel.builder().id(1L).build());

        return deliveries.stream().map(delivery -> modelMapper.map(delivery, DeliveryDto.class)).toList();
    }

    @Override
    @Transactional
    public DeliveryDto updateDeliveryStatus(Long deliveryId, DeliveryStatus deliveryStatus) {

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        delivery.setDeliveryStatus(deliveryStatus);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        if(deliveryStatus.equals(DeliveryStatus.DELIVERED)) {
            Order order = delivery.getOrder();
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            deliveryPersonnelService.updateDeliveryPersonnelAvailability(delivery.getDeliveryPersonnel(), true);
        }

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }
}
