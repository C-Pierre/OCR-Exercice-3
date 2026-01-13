package com.openclassrooms.starterjwt.teacher.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher savedTeacher;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ada");
        teacher.setLastName("Lovelace");

        teacherRepository.findByLastName(teacher.getLastName())
            .ifPresent(teacherRepository::delete);

        savedTeacher = teacherRepository.save(teacher);
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void findAll_shouldReturnTeachersFromDatabase() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].firstName").value("Ada"));
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void findById_shouldReturnTeacherFromDatabase() throws Exception {
        mockMvc.perform(get("/api/teacher/" + savedTeacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTeacher.getId()))
                .andExpect(jsonPath("$.firstName").value("Ada"));
    }
}
