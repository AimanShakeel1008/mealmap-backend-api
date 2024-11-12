package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Boolean active;
    private Set<Role> roles;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
