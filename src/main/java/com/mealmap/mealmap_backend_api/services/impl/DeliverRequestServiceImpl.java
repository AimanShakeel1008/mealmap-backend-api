package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.entities.Delivery;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.DeliveryRequest;
import com.mealmap.mealmap_backend_api.entities.Order;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.DeliveryPersonnelRepository;
import com.mealmap.mealmap_backend_api.respositories.DeliveryRepository;
import com.mealmap.mealmap_backend_api.respositories.DeliveryRequestRepository;
import com.mealmap.mealmap_backend_api.respositories.OrderRepository;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.services.DeliveryRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliverRequestServiceImpl implements DeliveryRequestService {

    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryPersonnelRepository deliveryPersonnelRepository;
    private final DeliveryPersonnelService deliveryPersonnelService;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DeliveryRequestDto> getAllDeliveryRequests() {
        List<DeliveryRequest> allDeliveryRequests = deliveryRequestRepository.findAll();

        return allDeliveryRequests
                .stream()
                .map(deliveryRequest -> modelMapper
                        .map(deliveryRequest, DeliveryRequestDto.class))
                .toList();
    }

    @Override
    @Transactional
    public DeliveryDto acceptDeliveryRequest(Long deliveryRequestId) {

        DeliveryPersonnel currentDeliveryPersonnel = deliveryPersonnelService.getCurrentDeliveryPersonnel();

        DeliveryRequest deliveryRequest = deliveryRequestRepository
                .findById(deliveryRequestId).orElseThrow(() -> new ResourceNotFoundException("Delivery Request not found"));

        DeliveryPersonnel deliveryPersonnel = deliveryPersonnelRepository
                .findById(currentDeliveryPersonnel.getId()).orElseThrow(() -> new ResourceNotFoundException("Delivery Personnel not found"));

        Delivery delivery = new Delivery();

        if (!deliveryPersonnel.getAvailable()) {
            throw new RuntimeConflictException("Cannot accept delivery request as delivery personnel is not available");
        }

        if (!deliveryRequest.getDeliveryRequestStatus().equals(DeliveryRequestStatus.PENDING)) {
            throw new RuntimeConflictException("Cannot accept delivery request as delivery request is not pending");
        }

        deliveryRequest.setDeliveryRequestStatus(DeliveryRequestStatus.CONFIRMED);
        deliveryRequestRepository.save(deliveryRequest);

        Order order = deliveryRequest.getOrder();

        delivery.setOrder(order);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERY_PERSON_ASSIGNED);
        delivery.setDeliveryPersonnel(deliveryPersonnel);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        deliveryPersonnelService.updateDeliveryPersonnelAvailability(deliveryPersonnel, false);

        return modelMapper.map(savedDelivery, DeliveryDto.class);
    }
}
