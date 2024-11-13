package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.entities.Delivery;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.Order;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentStatus;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPersonnelService deliveryPersonnelService;
    private final ModelMapper modelMapper;

    @Override
    public List<DeliveryDto> getAllDeliveriesOfADeliveryPersonnel() {
        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();
        List<Delivery>  deliveries = deliveryRepository.findByDeliveryPersonnel(currentDeliveryPersonnel);

        return deliveries.stream().map(delivery -> modelMapper.map(delivery, DeliveryDto.class)).toList();
    }

    @Override
    @Transactional
    public DeliveryDto updateDeliveryStatusToTheWayToRestaurant(Long deliveryId) {

        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_PERSON_ASSIGNED)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel not yet assigned.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.ON_THE_WAY_TO_RESTAURANT);

        Delivery savedDelivery = deliveryRepository.save(delivery);

//        if(deliveryStatus.equals(DeliveryStatus.DELIVERED)) {
//            Order order = delivery.getOrder();
//            order.setOrderStatus(OrderStatus.COMPLETED);
//            orderRepository.save(order);
//            deliveryPersonnelService.updateDeliveryPersonnelAvailability(delivery.getDeliveryPersonnel(), true);
//        }

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto getDeliveryByDeliveryId(Long deliveryId) {

        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();
        Delivery delivery = deliveryRepository.findByDeliveryPersonnelAndId(currentDeliveryPersonnel, deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
        return modelMapper.map(delivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto updateDeliveryStatusToAtTheRestaurant(Long deliveryId) {

        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.ON_THE_WAY_TO_RESTAURANT)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel yet not started the ride to restaurant.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.AT_RESTAURANT);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto updateDeliveryStatusToOrderPickedUp(Long deliveryId) {
        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.AT_RESTAURANT)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel has not reached the restaurant.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.ORDER_PICKED_UP);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto updateDeliveryStatusToOnTheWay(Long deliveryId) {
        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.ORDER_PICKED_UP)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel has not yet picked up the order.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.ON_THE_WAY);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto updateDeliveryStatusToReachedCustomerLocation(Long deliveryId) {
        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.ON_THE_WAY)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel has not yet his ride for customer location.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.REACHED_CUSTOMER_LOCATION);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }

    @Override
    public DeliveryDto updateDeliveryStatusToOrderDelivered(Long deliveryId) {
        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.REACHED_CUSTOMER_LOCATION)) {
            throw new RuntimeException("Cannot update the delivery status delivery personnel has not yet reached customer location.");
        }

        if(!Objects.equals(delivery.getDeliveryPersonnel().getId(), currentDeliveryPersonnel.getId())) {
            throw new RuntimeException("Cannot update the delivery status as you are not the assigned delivery personnel for this delivery.");
        }

        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);

        Order order = delivery.getOrder();
        if(order.getPaymentStatus().equals(PaymentStatus.PENDING) && order.getPaymentMode().equals(PaymentMode.CASH_ON_DELIVERY)) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        deliveryPersonnelService.updateDeliveryPersonnelAvailability(delivery.getDeliveryPersonnel(), true);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }
}
