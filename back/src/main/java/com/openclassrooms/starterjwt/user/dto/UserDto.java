package com.openclassrooms.starterjwt.user.dto;

import lombok.Data;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String lastName;
    private String firstName;
    private boolean admin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
