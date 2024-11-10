package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.DeliveryRequest;
import com.mealmap.mealmap_backend_api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {
    Optional<DeliveryRequest> findByOrder(Order order);
}
