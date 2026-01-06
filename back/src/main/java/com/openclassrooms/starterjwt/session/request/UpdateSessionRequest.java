package com.openclassrooms.starterjwt.session.request;

import lombok.Data;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateSessionRequest {

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
