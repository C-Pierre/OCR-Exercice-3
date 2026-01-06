package com.openclassrooms.starterjwt.session.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.openclassrooms.starterjwt.user.dto.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private Long id;
    private String name;
    private Date date;
    private Long teacherId;
    private String description;
    private List<UserDto> users;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
