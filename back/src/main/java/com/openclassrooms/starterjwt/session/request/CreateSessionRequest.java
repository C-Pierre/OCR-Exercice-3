package com.openclassrooms.starterjwt.session.request;

import lombok.Data;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateSessionRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private Date date;

    @NotNull
    private Long teacherId;

    @NotNull
    @Size(max = 2500)
    private String description;

    private List<Long> users;
}
