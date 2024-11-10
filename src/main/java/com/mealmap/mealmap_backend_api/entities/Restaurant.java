package com.mealmap.mealmap_backend_api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String hoursOfOperation;
    private String cuisineType;
    private String contactNumber;
    private Boolean open;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private RestaurantOwner owner;
}
