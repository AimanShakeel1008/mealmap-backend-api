package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.Delivery;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByDeliveryPersonnel(DeliveryPersonnel deliveryPersonnel);
}
