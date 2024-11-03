package com.mealmap.mealmap_backend_api.entities;

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

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @CreationTimestamp
    private LocalDateTime updatedTime;
}
