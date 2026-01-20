package com.openclassrooms.starterjwt.session.controller;

import java.util.List;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.user.model.User;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.session.model.Session;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.starterjwt.session.repository.SessionRepository;
import com.openclassrooms.starterjwt.session.request.CreateSessionRequest;
import com.openclassrooms.starterjwt.session.request.UpdateSessionRequest;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Session savedSession;
    private Teacher savedTeacher;
    private User savedUser;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAllSessionsParticipations();
        sessionRepository.deleteAll();

        Teacher teacher = new Teacher();
        teacher.setFirstName("Ada");
        teacher.setLastName("Lovelace");
        teacherRepository.findByLastName(teacher.getLastName())
                .ifPresent(teacherRepository::delete);
        savedTeacher = teacherRepository.save(teacher);

        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);
        userRepository.findByEmail(user.getEmail())
                .ifPresent(userRepository::delete);
        savedUser = userRepository.save(user);

        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("Relaxing yoga");
        session.setDate(new Date());
        session.setTeacher(savedTeacher);
        session.setUsers(List.of(savedUser));
        savedSession = sessionRepository.save(session);
    }

    @Test
    @WithMockUser
    void findById_shouldReturnSession() throws Exception {
        mockMvc.perform(get("/api/session/{id}", savedSession.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedSession.getId()))
                .andExpect(jsonPath("$.name").value("Yoga session"))
                .andExpect(jsonPath("$.teacherId").value(savedTeacher.getId()));
    }

    @Test
    @WithMockUser
    void findAll_shouldReturnSessions() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void create_shouldPersistSession() throws Exception {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("New session");
        request.setDescription("New description");
        request.setTeacherId(savedTeacher.getId());
        request.setDate(new Date());

        mockMvc.perform(post("/api/session")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New session"));

        assertThat (sessionRepository.findAll()).hasSize(2);
    }

    @Test
    @WithMockUser
    void update_shouldModifySession() throws Exception {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Updated name");
        request.setDescription("Updated desc");
        request.setTeacherId(savedTeacher.getId());
        request.setDate(new Date());

        mockMvc.perform(put("/api/session/{id}", savedSession.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"));
    }

    @Test
    @WithMockUser
    void delete_shouldRemoveSession() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", savedSession.getId()))
                .andExpect(status().isNoContent());

        assertThat(sessionRepository.findById(savedSession.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username="test@test.com")
    void participate_shouldAddParticipation() throws Exception {
        User newUser = new User();
        newUser.setEmail("new@test.com");
        newUser.setFirstName("Jane");
        newUser.setLastName("Doe");
        newUser.setPassword("pass");
        newUser.setAdmin(false);
        userRepository.save(newUser);

        mockMvc.perform(post("/api/session/{id}/participate/{userId}",
                        savedSession.getId(),
                        newUser.getId()))
                .andExpect(status().isOk());

        Session updated = sessionRepository.findById(savedSession.getId()).orElseThrow();
        assertThat(updated.getUsers()).contains(newUser);
    }

    @Test
    @WithMockUser(username="test@test.com")
    void noLongerParticipate_shouldRemoveParticipation() throws Exception {
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}",
                        savedSession.getId(),
                        savedUser.getId()))
                .andExpect(status().isOk());

        Session updated = sessionRepository.findById(savedSession.getId()).orElseThrow();
        assertThat (updated.getUsers()).doesNotContain(savedUser);
    }

    @Test
    @WithMockUser(username="unknown@test.com")
    void participate_shouldReturn400_whenUserNotFound() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}",
                        savedSession.getId(),
                        99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="unknown@test.com")
    void noLongerParticipate_shouldReturn400_whenUserNotInSession() throws Exception {
        User newUser = new User();
        newUser.setEmail("new2@test.com");
        newUser.setFirstName("Alice");
        newUser.setLastName("Doe");
        newUser.setPassword("pass");
        newUser.setAdmin(false);
        userRepository.save(newUser);

        mockMvc.perform(
            delete(
                "/api/session/{id}/participate/{userId}",
                savedSession.getId(),
                newUser.getId()
            )
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void findById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
}
