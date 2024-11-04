package com.mealmap.mealmap_backend_api.entities;

import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Driver;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private  Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeliveryPersonnel deliveryPersonnel;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;



}
