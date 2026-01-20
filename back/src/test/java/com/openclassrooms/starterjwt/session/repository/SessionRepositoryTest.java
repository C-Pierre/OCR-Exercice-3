package com.openclassrooms.starterjwt.session.repository;

import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveSessionWithTeacherAndUsers() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ada");
        teacher.setLastName("Lovelace");
        teacher = teacherRepository.save(teacher);

        User u1 = new User();
        u1.setEmail("a@test.com");
        u1.setFirstName("Jean");
        u1.setLastName("Paul");
        u1.setPassword("Password");
        u1 = userRepository.save(u1);

        User u2 = new User();
        u2.setEmail("b@test.com");
        u2.setFirstName("Aline");
        u2.setLastName("Jean");
        u2.setPassword("Password");
        u2 = userRepository.save(u2);

        Session session = Session.builder()
                .name("Spring Boot")
                .description("Test session")
                .date(new Date())
                .teacher(teacher)
                .users(List.of(u1, u2))
                .build();

        Session saved = sessionRepository.save(session);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(saved.getUsers()).hasSize(2);
    }
}
