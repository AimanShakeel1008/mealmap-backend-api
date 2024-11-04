package com.mealmap.mealmap_backend_api.entities;

import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    private DeliveryRequestStatus deliveryRequestStatus;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @CreationTimestamp
    private LocalDateTime updatedTime;
}
